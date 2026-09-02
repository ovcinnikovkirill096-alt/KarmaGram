package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.RemoteControlClient;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.exteragram.messenger.utils.AppUtils;
import java.io.File;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.LaunchActivity;
import org.webrtc.MediaStreamTrack;

public class MusicPlayerService extends Service implements NotificationCenter.NotificationCenterDelegate {
    private static final int ID_NOTIFICATION = 5;
    public static final String NOTIFY_CLOSE = "org.telegram.android.musicplayer.close";
    public static final String NOTIFY_NEXT = "org.telegram.android.musicplayer.next";
    public static final String NOTIFY_PAUSE = "org.telegram.android.musicplayer.pause";
    public static final String NOTIFY_PLAY = "org.telegram.android.musicplayer.play";
    public static final String NOTIFY_PREVIOUS = "org.telegram.android.musicplayer.previous";
    public static final String NOTIFY_SEEK = "org.telegram.android.musicplayer.seek";
    private static boolean supportBigNotifications = true;
    private static boolean supportLockScreenControls = !TextUtils.isEmpty(AndroidUtilities.getSystemProperty("ro.miui.ui.version.code"));
    private Bitmap albumArtPlaceholder;
    private AudioManager audioManager;
    private boolean foregroundServiceIsStarted;
    private BroadcastReceiver headsetPlugReceiver = new BroadcastReceiver() { // from class: org.telegram.messenger.MusicPlayerService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.media.AUDIO_BECOMING_NOISY".equals(intent.getAction())) {
                MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
            }
        }
    };
    private ImageReceiver imageReceiver;
    private String loadingFilePath;
    private MediaSession mediaSession;
    private int notificationMessageID;
    private PlaybackState.Builder playbackState;
    private RemoteControlClient remoteControlClient;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        this.audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        for (int i = 0; i < 16; i++) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingDidSeek);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileLoaded);
        }
        ImageReceiver imageReceiver = new ImageReceiver(null);
        this.imageReceiver = imageReceiver;
        imageReceiver.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.messenger.MusicPlayerService$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public final void didSetImage(ImageReceiver imageReceiver2, boolean z, boolean z2, boolean z3) {
                this.f$0.lambda$onCreate$0(imageReceiver2, z, z2, z3);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void didSetImageBitmap(int i2, String str, Drawable drawable) {
                ImageReceiver.ImageReceiverDelegate.CC.$default$didSetImageBitmap(this, i2, str, drawable);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver2) {
                ImageReceiver.ImageReceiverDelegate.CC.$default$onAnimationReady(this, imageReceiver2);
            }
        });
        this.mediaSession = new MediaSession(this, "telegramAudioPlayer");
        this.playbackState = new PlaybackState.Builder();
        this.albumArtPlaceholder = Bitmap.createBitmap(AndroidUtilities.dp(102.0f), AndroidUtilities.dp(102.0f), Bitmap.Config.ARGB_8888);
        Drawable drawable = getResources().getDrawable(R.drawable.nocover_big);
        drawable.setBounds(0, 0, this.albumArtPlaceholder.getWidth(), this.albumArtPlaceholder.getHeight());
        drawable.draw(new Canvas(this.albumArtPlaceholder));
        this.mediaSession.setCallback(new MediaSession.Callback() { // from class: org.telegram.messenger.MusicPlayerService.2
            @Override // android.media.session.MediaSession.Callback
            public void onStop() {
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPlay() {
                MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
            }

            @Override // android.media.session.MediaSession.Callback
            public void onPause() {
                MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSkipToNext() {
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject == null || !playingMessageObject.isMusic()) {
                    return;
                }
                MediaController.getInstance().playNextMessage();
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSkipToPrevious() {
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject == null || !playingMessageObject.isMusic()) {
                    return;
                }
                MediaController.getInstance().playPreviousMessage();
            }

            @Override // android.media.session.MediaSession.Callback
            public void onSeekTo(long j) {
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null) {
                    MediaController.getInstance().seekToProgress(playingMessageObject, (j / 1000) / ((float) playingMessageObject.getDuration()));
                    MusicPlayerService.this.updatePlaybackState(j);
                }
            }
        });
        this.mediaSession.setActive(true);
        registerReceiver(this.headsetPlugReceiver, new IntentFilter("android.media.AUDIO_BECOMING_NOISY"));
        super.onCreate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        if (!z || TextUtils.isEmpty(this.loadingFilePath)) {
            return;
        }
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null) {
            createNotification(playingMessageObject, true);
        }
        this.loadingFilePath = null;
    }

    @Override // android.app.Service
    @SuppressLint({"NewApi"})
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            try {
                if ((getPackageName() + ".STOP_PLAYER").equals(intent.getAction())) {
                    MediaController.getInstance().cleanupPlayer(true, true);
                    return 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MusicPlayerService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.stopSelf();
                }
            });
            return 1;
        }
        if (supportLockScreenControls) {
            ComponentName componentName = new ComponentName(getApplicationContext(), MusicPlayerReceiver.class.getName());
            try {
                if (this.remoteControlClient == null) {
                    this.audioManager.registerMediaButtonEventReceiver(componentName);
                    Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON");
                    intent2.setComponent(componentName);
                    RemoteControlClient remoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(this, 0, intent2, fixIntentFlags(33554432)));
                    this.remoteControlClient = remoteControlClient;
                    this.audioManager.registerRemoteControlClient(remoteControlClient);
                }
                this.remoteControlClient.setTransportControlFlags(189);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        createNotification(playingMessageObject, false);
        return 1;
    }

    private Bitmap loadArtworkFromUrl(String str, boolean z, boolean z2) {
        ImageLoader.getHttpFileName(str);
        File httpFilePath = ImageLoader.getHttpFilePath(str, "jpg");
        if (httpFilePath.exists()) {
            return ImageLoader.loadBitmap(httpFilePath.getAbsolutePath(), null, z ? 600.0f : 100.0f, z ? 600.0f : 100.0f, false);
        }
        if (z2) {
            this.loadingFilePath = httpFilePath.getAbsolutePath();
            if (!z) {
                this.imageReceiver.setImage(str, "48_48", null, null, 0L);
            }
        } else {
            this.loadingFilePath = null;
        }
        return null;
    }

    private Bitmap getAvatarBitmap(TLObject tLObject, boolean z, boolean z2) {
        AvatarDrawable avatarDrawable;
        int i = z ? 600 : 100;
        try {
            if (tLObject instanceof TLRPC.User) {
                TLRPC.User user = (TLRPC.User) tLObject;
                TLRPC.FileLocation fileLocation = z ? user.photo.photo_big : user.photo.photo_small;
                if (fileLocation != null) {
                    File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(fileLocation, true);
                    if (pathToAttach.exists()) {
                        float f = i;
                        return ImageLoader.loadBitmap(pathToAttach.getAbsolutePath(), null, f, f, false);
                    }
                    if (z) {
                        if (z2) {
                            this.loadingFilePath = FileLoader.getAttachFileName(fileLocation);
                            this.imageReceiver.setImage(ImageLocation.getForUser(user, 0), _UrlKt.FRAGMENT_ENCODE_SET, null, null, null, 0);
                        } else {
                            this.loadingFilePath = null;
                        }
                    }
                }
            } else {
                TLRPC.Chat chat = (TLRPC.Chat) tLObject;
                TLRPC.FileLocation fileLocation2 = z ? chat.photo.photo_big : chat.photo.photo_small;
                if (fileLocation2 != null) {
                    File pathToAttach2 = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(fileLocation2, true);
                    if (pathToAttach2.exists()) {
                        float f2 = i;
                        return ImageLoader.loadBitmap(pathToAttach2.getAbsolutePath(), null, f2, f2, false);
                    }
                    if (z) {
                        if (z2) {
                            this.loadingFilePath = FileLoader.getAttachFileName(fileLocation2);
                            this.imageReceiver.setImage(ImageLocation.getForChat(chat, 0), _UrlKt.FRAGMENT_ENCODE_SET, null, null, null, 0);
                        } else {
                            this.loadingFilePath = null;
                        }
                    }
                }
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
        if (z) {
            return null;
        }
        Theme.createDialogsResources(this);
        if (tLObject instanceof TLRPC.User) {
            avatarDrawable = new AvatarDrawable((TLRPC.User) tLObject);
        } else {
            avatarDrawable = new AvatarDrawable((TLRPC.Chat) tLObject);
        }
        avatarDrawable.setRoundRadius(1);
        float f3 = i;
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Bitmap.Config.ARGB_8888);
        avatarDrawable.setBounds(0, 0, bitmapCreateBitmap.getWidth(), bitmapCreateBitmap.getHeight());
        avatarDrawable.draw(new Canvas(bitmapCreateBitmap));
        return bitmapCreateBitmap;
    }

    /* JADX WARN: Code duplicated, block: B:100:0x032f  */
    /* JADX WARN: Code duplicated, block: B:103:0x0358  */
    /* JADX WARN: Code duplicated, block: B:105:0x036c  */
    /* JADX WARN: Code duplicated, block: B:107:0x0375  */
    /* JADX WARN: Code duplicated, block: B:109:0x037a  */
    /* JADX WARN: Code duplicated, block: B:112:0x0381  */
    /* JADX WARN: Code duplicated, block: B:113:0x0383  */
    /* JADX WARN: Code duplicated, block: B:116:0x03a2  */
    /* JADX WARN: Code duplicated, block: B:118:0x03a9  */
    /* JADX WARN: Code duplicated, block: B:121:0x03b2  */
    /* JADX WARN: Code duplicated, block: B:124:0x03c4  */
    /* JADX WARN: Code duplicated, block: B:126:0x03c9  */
    /* JADX WARN: Code duplicated, block: B:129:0x03dc  */
    /* JADX WARN: Code duplicated, block: B:135:0x0425  */
    /* JADX WARN: Code duplicated, block: B:141:0x043c  */
    /* JADX WARN: Code duplicated, block: B:144:0x0456  */
    /* JADX WARN: Code duplicated, block: B:146:0x045a  */
    /* JADX WARN: Code duplicated, block: B:147:0x0460  */
    /* JADX WARN: Code duplicated, block: B:148:0x046a A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:149:0x046c  */
    /* JADX WARN: Code duplicated, block: B:150:0x0470  */
    /* JADX WARN: Code duplicated, block: B:153:0x0481  */
    /* JADX WARN: Code duplicated, block: B:155:0x0493  */
    /* JADX WARN: Code duplicated, block: B:162:0x04c9  */
    /* JADX WARN: Code duplicated, block: B:168:0x04e1  */
    /* JADX WARN: Code duplicated, block: B:171:0x04ec  */
    /* JADX WARN: Code duplicated, block: B:172:0x04f4  */
    /* JADX WARN: Code duplicated, block: B:174:0x051c  */
    /* JADX WARN: Code duplicated, block: B:175:0x051e  */
    /* JADX WARN: Code duplicated, block: B:178:0x053e  */
    /* JADX WARN: Code duplicated, block: B:179:0x0540  */
    /* JADX WARN: Code duplicated, block: B:184:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:64:0x018c  */
    /* JADX WARN: Code duplicated, block: B:67:0x0194  */
    /* JADX WARN: Code duplicated, block: B:68:0x019b  */
    /* JADX WARN: Code duplicated, block: B:71:0x020f  */
    /* JADX WARN: Code duplicated, block: B:72:0x0212  */
    /* JADX WARN: Code duplicated, block: B:75:0x027e  */
    /* JADX WARN: Code duplicated, block: B:76:0x0289  */
    /* JADX WARN: Code duplicated, block: B:80:0x0297  */
    /* JADX WARN: Code duplicated, block: B:86:0x02c2  */
    /* JADX WARN: Code duplicated, block: B:89:0x02e5  */
    /* JADX WARN: Code duplicated, block: B:92:0x02f0  */
    /* JADX WARN: Code duplicated, block: B:94:0x02fa  */
    /* JADX WARN: Code duplicated, block: B:95:0x02fe  */
    /* JADX WARN: Code duplicated, block: B:98:0x031b  */
    @SuppressLint({"NewApi"})
    private void createNotification(MessageObject messageObject, boolean z) {
        long j;
        Bitmap avatarBitmap;
        Bitmap smallCover;
        Bitmap avatarBitmap2;
        Bitmap cover;
        Bitmap bitmap;
        int i;
        boolean zIsMessagePaused;
        boolean z2;
        PendingIntent broadcast;
        Bitmap bitmap2;
        String str;
        PendingIntent broadcast2;
        PendingIntent broadcast3;
        Notification.MediaStyle mediaSession;
        PendingIntent pendingIntent;
        Notification.Builder builder;
        String album;
        String string;
        String string2;
        long j2;
        long j3;
        int i2;
        String str2;
        String str3;
        int i3;
        int i4;
        String str4;
        String str5;
        String album2;
        Bitmap bitmap3;
        Notification notificationBuild;
        int id;
        int i5;
        int i6;
        float f;
        RemoteControlClient.MetadataEditor metadataEditorEditMetadata;
        String musicTitle = messageObject.getMusicTitle();
        String musicAuthor = messageObject.getMusicAuthor();
        AudioInfo audioInfo = MediaController.getInstance().getAudioInfo();
        Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
        if (messageObject.isMusic()) {
            intent.setAction("com.tmessages.openplayer");
            intent.addCategory("android.intent.category.LAUNCHER");
        } else if (messageObject.isVoice() || messageObject.isRoundVideo()) {
            intent.setAction("android.intent.action.VIEW");
            TLRPC.Peer peer = messageObject.messageOwner.peer_id;
            if (peer instanceof TLRPC.TL_peerUser) {
                j = peer.user_id;
            } else if (peer instanceof TLRPC.TL_peerChat) {
                j = peer.chat_id;
            } else {
                j = peer instanceof TLRPC.TL_peerChannel ? peer.channel_id : 0L;
            }
            if (j != 0) {
                if (peer instanceof TLRPC.TL_peerUser) {
                    intent.setData(Uri.parse("tg://openmessage?user_id=" + j + "&message_id=" + messageObject.getId()));
                } else {
                    intent.setData(Uri.parse("tg://openmessage?chat_id=" + j + "&message_id=" + messageObject.getId()));
                }
            }
        }
        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, fixIntentFlags(33554432));
        long duration = (long) (messageObject.getDuration() * 1000.0d);
        if (messageObject.isMusic()) {
            String artworkUrl = messageObject.getArtworkUrl(true);
            String artworkUrl2 = messageObject.getArtworkUrl(false);
            smallCover = audioInfo != null ? audioInfo.getSmallCover() : null;
            cover = audioInfo != null ? audioInfo.getCover() : null;
            this.loadingFilePath = null;
            this.imageReceiver.setImageBitmap((Drawable) null);
            if (smallCover == null && !TextUtils.isEmpty(artworkUrl)) {
                cover = loadArtworkFromUrl(artworkUrl2, true, !z);
                if (cover == null) {
                    smallCover = loadArtworkFromUrl(artworkUrl, false, !z);
                    cover = smallCover;
                } else {
                    smallCover = loadArtworkFromUrl(artworkUrl2, false, !z);
                }
            } else {
                this.loadingFilePath = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(messageObject.getDocument()).getAbsolutePath();
            }
        } else {
            if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                long senderId = messageObject.getSenderId();
                if (messageObject.isFromUser()) {
                    TLRPC.User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(senderId));
                    if (user != null) {
                        musicTitle = UserObject.getUserName(user);
                        avatarBitmap = getAvatarBitmap(user, true, !z);
                        avatarBitmap2 = getAvatarBitmap(user, false, !z);
                    } else {
                        avatarBitmap2 = null;
                        avatarBitmap = null;
                    }
                } else {
                    TLRPC.Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-senderId));
                    if (chat != null) {
                        musicTitle = chat.title;
                        avatarBitmap = getAvatarBitmap(chat, true, !z);
                        avatarBitmap2 = getAvatarBitmap(chat, false, !z);
                    } else {
                        avatarBitmap = null;
                        smallCover = null;
                    }
                    if (avatarBitmap == null || smallCover == null) {
                        cover = avatarBitmap;
                    } else {
                        cover = smallCover;
                    }
                    if (messageObject.isVoice()) {
                        musicAuthor = LocaleController.getString(R.string.AttachAudio);
                    } else {
                        musicAuthor = LocaleController.getString(R.string.AttachRound);
                    }
                }
                smallCover = avatarBitmap2;
                if (avatarBitmap == null) {
                    cover = avatarBitmap;
                } else {
                    cover = avatarBitmap;
                }
                if (messageObject.isVoice()) {
                    musicAuthor = LocaleController.getString(R.string.AttachAudio);
                } else {
                    musicAuthor = LocaleController.getString(R.string.AttachRound);
                }
            } else {
                bitmap = null;
                smallCover = null;
            }
            i = Build.VERSION.SDK_INT;
            zIsMessagePaused = MediaController.getInstance().isMessagePaused();
            z2 = !zIsMessagePaused;
            broadcast = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PREVIOUS).setComponent(new ComponentName(this, (Class<?>) MusicPlayerReceiver.class)), fixIntentFlags(301989888));
            Context applicationContext = getApplicationContext();
            Intent intent2 = new Intent(this, getClass());
            StringBuilder sb = new StringBuilder();
            bitmap2 = bitmap;
            sb.append(getPackageName());
            sb.append(".STOP_PLAYER");
            PendingIntent service = PendingIntent.getService(applicationContext, 0, intent2.setAction(sb.toString()), fixIntentFlags(301989888));
            Context applicationContext2 = getApplicationContext();
            if (zIsMessagePaused) {
                str = NOTIFY_PLAY;
            } else {
                str = NOTIFY_PAUSE;
            }
            broadcast2 = PendingIntent.getBroadcast(applicationContext2, 0, new Intent(str).setComponent(new ComponentName(this, (Class<?>) MusicPlayerReceiver.class)), fixIntentFlags(301989888));
            broadcast3 = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_NEXT).setComponent(new ComponentName(this, (Class<?>) MusicPlayerReceiver.class)), fixIntentFlags(301989888));
            PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_SEEK).setComponent(new ComponentName(this, (Class<?>) MusicPlayerReceiver.class)), fixIntentFlags(301989888));
            mediaSession = new Notification.MediaStyle().setMediaSession(this.mediaSession.getSessionToken());
            if (messageObject.isMusic()) {
                pendingIntent = broadcast2;
                mediaSession.setShowActionsInCompactView(0, 1, 2);
            } else {
                pendingIntent = broadcast2;
                if (!messageObject.isVoice() || messageObject.isRoundVideo()) {
                    mediaSession.setShowActionsInCompactView(0);
                }
            }
            builder = new Notification.Builder(this);
            Notification.Builder contentText = builder.setSmallIcon(R.drawable.notification).setOngoing(z2).setContentTitle(musicTitle).setContentText(musicAuthor);
            if (audioInfo == null && messageObject.isMusic()) {
                album = audioInfo.getAlbum();
            } else {
                album = null;
            }
            contentText.setSubText(album).setContentIntent(activity).setDeleteIntent(service).setShowWhen(false).setCategory("transport").setPriority(2).setStyle(mediaSession);
            if (i < 31) {
                builder.setColor(AppUtils.getNotificationColor());
            }
            if (i >= 26) {
                NotificationsController.checkOtherNotificationsChannel();
                builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            }
            if (smallCover != null) {
                builder.setLargeIcon(smallCover);
            } else {
                builder.setLargeIcon(this.albumArtPlaceholder);
            }
            string = LocaleController.getString(R.string.Next);
            string2 = LocaleController.getString(R.string.AccDescrPrevious);
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                j2 = 1000;
                this.playbackState.setState(6, 0L, 1.0f).setActions(0L);
                if (messageObject.isMusic()) {
                    builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_previous, string2, broadcast).build());
                }
                builder.addAction(new Notification.Action.Builder(R.drawable.loading_animation2, LocaleController.getString(R.string.Loading), (PendingIntent) null).build());
                if (messageObject.isMusic()) {
                    builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_next, string, broadcast3).build());
                }
                str2 = musicTitle;
                str3 = musicAuthor;
            } else {
                j2 = 1000;
                if (messageObject.isMusic()) {
                    j3 = 822;
                } else {
                    j3 = 774;
                }
                long j4 = j3;
                PlaybackState.Builder builder2 = this.playbackState;
                if (zIsMessagePaused) {
                    i2 = 2;
                } else {
                    i2 = 3;
                }
                str2 = musicTitle;
                str3 = musicAuthor;
                builder2.setState(i2, ((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, getPlaybackSpeed(z2, messageObject)).setActions(j4);
                if (zIsMessagePaused) {
                    i3 = R.string.AccActionPlay;
                } else {
                    i3 = R.string.AccActionPause;
                }
                String string3 = LocaleController.getString(i3);
                if (messageObject.isMusic()) {
                    builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_previous, string2, broadcast).build());
                }
                if (zIsMessagePaused) {
                    i4 = R.drawable.ic_action_play;
                } else {
                    i4 = R.drawable.ic_action_pause;
                }
                builder.addAction(new Notification.Action.Builder(i4, string3, pendingIntent).build());
                if (messageObject.isMusic()) {
                    builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_next, string, broadcast3).build());
                }
            }
            this.mediaSession.setPlaybackState(this.playbackState.build());
            str4 = str3;
            str5 = str2;
            MediaMetadata.Builder builderPutString = new MediaMetadata.Builder().putString("android.media.metadata.ALBUM_ARTIST", str4).putString("android.media.metadata.ARTIST", str4).putLong("android.media.metadata.DURATION", duration).putString("android.media.metadata.TITLE", str5);
            if (audioInfo == null && messageObject.isMusic()) {
                album2 = audioInfo.getAlbum();
            } else {
                album2 = null;
            }
            MediaMetadata.Builder builderPutString2 = builderPutString.putString("android.media.metadata.ALBUM", album2);
            if (bitmap2 != null || bitmap2.isRecycled()) {
                bitmap3 = bitmap2;
            } else {
                bitmap3 = bitmap2;
                builderPutString2.putBitmap("android.media.metadata.ALBUM_ART", bitmap3);
            }
            this.mediaSession.setMetadata(builderPutString2.build());
            builder.setVisibility(1);
            notificationBuild = builder.build();
            if (i >= 31) {
                if (!this.foregroundServiceIsStarted) {
                    this.foregroundServiceIsStarted = true;
                    startForeground(5, notificationBuild);
                } else {
                    ((NotificationManager) getSystemService("notification")).notify(5, notificationBuild);
                }
            } else if (!zIsMessagePaused) {
                startForeground(5, notificationBuild);
            } else {
                stopForeground(false);
                ((NotificationManager) getSystemService("notification")).notify(5, notificationBuild);
            }
            if (this.remoteControlClient != null) {
                id = MediaController.getInstance().getPlayingMessageObject().getId();
                if (this.notificationMessageID != id) {
                    this.notificationMessageID = id;
                    metadataEditorEditMetadata = this.remoteControlClient.editMetadata(true);
                    i5 = 2;
                    metadataEditorEditMetadata.putString(2, str4);
                    metadataEditorEditMetadata.putString(7, str5);
                    if (audioInfo != null && !TextUtils.isEmpty(audioInfo.getAlbum())) {
                        metadataEditorEditMetadata.putString(1, audioInfo.getAlbum());
                    }
                    metadataEditorEditMetadata.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * j2);
                    if (bitmap3 != null) {
                        try {
                            metadataEditorEditMetadata.putBitmap(100, bitmap3);
                        } catch (Throwable th) {
                            FileLog.e(th);
                        }
                    }
                    metadataEditorEditMetadata.apply();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MusicPlayerService.3
                        @Override // java.lang.Runnable
                        public void run() {
                            if (MusicPlayerService.this.remoteControlClient == null || MediaController.getInstance().getPlayingMessageObject() == null) {
                                return;
                            }
                            if (MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration == -9223372036854775807L) {
                                AndroidUtilities.runOnUIThread(this, 500L);
                                return;
                            }
                            RemoteControlClient.MetadataEditor metadataEditorEditMetadata2 = MusicPlayerService.this.remoteControlClient.editMetadata(false);
                            metadataEditorEditMetadata2.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * 1000);
                            metadataEditorEditMetadata2.apply();
                            MusicPlayerService.this.remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3, Math.max(((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, 100L), MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
                        }
                    }, j2);
                } else {
                    i5 = 2;
                }
                if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                    this.remoteControlClient.setPlaybackState(8);
                    return;
                }
                RemoteControlClient.MetadataEditor metadataEditorEditMetadata2 = this.remoteControlClient.editMetadata(false);
                metadataEditorEditMetadata2.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * 1000);
                metadataEditorEditMetadata2.apply();
                RemoteControlClient remoteControlClient = this.remoteControlClient;
                if (MediaController.getInstance().isMessagePaused()) {
                    i6 = i5;
                } else {
                    i6 = 3;
                }
                long jMax = Math.max(((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, 100L);
                if (MediaController.getInstance().isMessagePaused()) {
                    f = 0.0f;
                } else {
                    f = 1.0f;
                }
                remoteControlClient.setPlaybackState(i6, jMax, f);
            }
        }
        bitmap = cover;
        i = Build.VERSION.SDK_INT;
        zIsMessagePaused = MediaController.getInstance().isMessagePaused();
        z2 = !zIsMessagePaused;
        broadcast = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PREVIOUS).setComponent(new ComponentName(this, (Class<?>) MusicPlayerReceiver.class)), fixIntentFlags(301989888));
        Context applicationContext3 = getApplicationContext();
        Intent intent3 = new Intent(this, getClass());
        StringBuilder sb2 = new StringBuilder();
        bitmap2 = bitmap;
        sb2.append(getPackageName());
        sb2.append(".STOP_PLAYER");
        PendingIntent service2 = PendingIntent.getService(applicationContext3, 0, intent3.setAction(sb2.toString()), fixIntentFlags(301989888));
        Context applicationContext4 = getApplicationContext();
        if (zIsMessagePaused) {
            str = NOTIFY_PAUSE;
        } else {
            str = NOTIFY_PLAY;
        }
        broadcast2 = PendingIntent.getBroadcast(applicationContext4, 0, new Intent(str).setComponent(new ComponentName(this, (Class<?>) MusicPlayerReceiver.class)), fixIntentFlags(301989888));
        broadcast3 = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_NEXT).setComponent(new ComponentName(this, (Class<?>) MusicPlayerReceiver.class)), fixIntentFlags(301989888));
        PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_SEEK).setComponent(new ComponentName(this, (Class<?>) MusicPlayerReceiver.class)), fixIntentFlags(301989888));
        mediaSession = new Notification.MediaStyle().setMediaSession(this.mediaSession.getSessionToken());
        if (messageObject.isMusic()) {
            pendingIntent = broadcast2;
            mediaSession.setShowActionsInCompactView(0, 1, 2);
        } else {
            pendingIntent = broadcast2;
            if (!messageObject.isVoice()) {
                mediaSession.setShowActionsInCompactView(0);
            } else {
                mediaSession.setShowActionsInCompactView(0);
            }
        }
        builder = new Notification.Builder(this);
        Notification.Builder contentText2 = builder.setSmallIcon(R.drawable.notification).setOngoing(z2).setContentTitle(musicTitle).setContentText(musicAuthor);
        if (audioInfo == null) {
            album = null;
        } else {
            album = null;
        }
        contentText2.setSubText(album).setContentIntent(activity).setDeleteIntent(service2).setShowWhen(false).setCategory("transport").setPriority(2).setStyle(mediaSession);
        if (i < 31) {
            builder.setColor(AppUtils.getNotificationColor());
        }
        if (i >= 26) {
            NotificationsController.checkOtherNotificationsChannel();
            builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
        }
        if (smallCover != null) {
            builder.setLargeIcon(smallCover);
        } else {
            builder.setLargeIcon(this.albumArtPlaceholder);
        }
        string = LocaleController.getString(R.string.Next);
        string2 = LocaleController.getString(R.string.AccDescrPrevious);
        if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            j2 = 1000;
            this.playbackState.setState(6, 0L, 1.0f).setActions(0L);
            if (messageObject.isMusic()) {
                builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_previous, string2, broadcast).build());
            }
            builder.addAction(new Notification.Action.Builder(R.drawable.loading_animation2, LocaleController.getString(R.string.Loading), (PendingIntent) null).build());
            if (messageObject.isMusic()) {
                builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_next, string, broadcast3).build());
            }
            str2 = musicTitle;
            str3 = musicAuthor;
        } else {
            j2 = 1000;
            if (messageObject.isMusic()) {
                j3 = 822;
            } else {
                j3 = 774;
            }
            long j5 = j3;
            PlaybackState.Builder builder3 = this.playbackState;
            if (zIsMessagePaused) {
                i2 = 3;
            } else {
                i2 = 2;
            }
            str2 = musicTitle;
            str3 = musicAuthor;
            builder3.setState(i2, ((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, getPlaybackSpeed(z2, messageObject)).setActions(j5);
            if (zIsMessagePaused) {
                i3 = R.string.AccActionPause;
            } else {
                i3 = R.string.AccActionPlay;
            }
            String string4 = LocaleController.getString(i3);
            if (messageObject.isMusic()) {
                builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_previous, string2, broadcast).build());
            }
            if (zIsMessagePaused) {
                i4 = R.drawable.ic_action_pause;
            } else {
                i4 = R.drawable.ic_action_play;
            }
            builder.addAction(new Notification.Action.Builder(i4, string4, pendingIntent).build());
            if (messageObject.isMusic()) {
                builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_next, string, broadcast3).build());
            }
        }
        this.mediaSession.setPlaybackState(this.playbackState.build());
        str4 = str3;
        str5 = str2;
        MediaMetadata.Builder builderPutString3 = new MediaMetadata.Builder().putString("android.media.metadata.ALBUM_ARTIST", str4).putString("android.media.metadata.ARTIST", str4).putLong("android.media.metadata.DURATION", duration).putString("android.media.metadata.TITLE", str5);
        if (audioInfo == null) {
            album2 = null;
        } else {
            album2 = null;
        }
        MediaMetadata.Builder builderPutString4 = builderPutString3.putString("android.media.metadata.ALBUM", album2);
        if (bitmap2 != null) {
            bitmap3 = bitmap2;
        } else {
            bitmap3 = bitmap2;
        }
        this.mediaSession.setMetadata(builderPutString4.build());
        builder.setVisibility(1);
        notificationBuild = builder.build();
        if (i >= 31) {
            if (!this.foregroundServiceIsStarted) {
                this.foregroundServiceIsStarted = true;
                startForeground(5, notificationBuild);
            } else {
                ((NotificationManager) getSystemService("notification")).notify(5, notificationBuild);
            }
        } else if (!zIsMessagePaused) {
            startForeground(5, notificationBuild);
        } else {
            stopForeground(false);
            ((NotificationManager) getSystemService("notification")).notify(5, notificationBuild);
        }
        if (this.remoteControlClient != null) {
            id = MediaController.getInstance().getPlayingMessageObject().getId();
            if (this.notificationMessageID != id) {
                this.notificationMessageID = id;
                metadataEditorEditMetadata = this.remoteControlClient.editMetadata(true);
                i5 = 2;
                metadataEditorEditMetadata.putString(2, str4);
                metadataEditorEditMetadata.putString(7, str5);
                if (audioInfo != null) {
                    metadataEditorEditMetadata.putString(1, audioInfo.getAlbum());
                }
                metadataEditorEditMetadata.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * j2);
                if (bitmap3 != null) {
                    metadataEditorEditMetadata.putBitmap(100, bitmap3);
                }
                metadataEditorEditMetadata.apply();
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MusicPlayerService.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (MusicPlayerService.this.remoteControlClient == null || MediaController.getInstance().getPlayingMessageObject() == null) {
                            return;
                        }
                        if (MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration == -9223372036854775807L) {
                            AndroidUtilities.runOnUIThread(this, 500L);
                            return;
                        }
                        RemoteControlClient.MetadataEditor metadataEditorEditMetadata3 = MusicPlayerService.this.remoteControlClient.editMetadata(false);
                        metadataEditorEditMetadata3.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * 1000);
                        metadataEditorEditMetadata3.apply();
                        MusicPlayerService.this.remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3, Math.max(((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, 100L), MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
                    }
                }, j2);
            } else {
                i5 = 2;
            }
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                this.remoteControlClient.setPlaybackState(8);
                return;
            }
            RemoteControlClient.MetadataEditor metadataEditorEditMetadata3 = this.remoteControlClient.editMetadata(false);
            metadataEditorEditMetadata3.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * 1000);
            metadataEditorEditMetadata3.apply();
            RemoteControlClient remoteControlClient2 = this.remoteControlClient;
            if (MediaController.getInstance().isMessagePaused()) {
                i6 = i5;
            } else {
                i6 = 3;
            }
            long jMax2 = Math.max(((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, 100L);
            if (MediaController.getInstance().isMessagePaused()) {
                f = 0.0f;
            } else {
                f = 1.0f;
            }
            remoteControlClient2.setPlaybackState(i6, jMax2, f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlaybackState(long j) {
        boolean zIsMessagePaused = MediaController.getInstance().isMessagePaused();
        boolean z = !zIsMessagePaused;
        if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            this.playbackState.setState(6, 0L, 1.0f).setActions(0L);
        } else {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            this.playbackState.setState(!zIsMessagePaused ? 3 : 2, j, getPlaybackSpeed(z, playingMessageObject)).setActions((playingMessageObject == null || !playingMessageObject.isMusic()) ? 774L : 822L);
        }
        this.mediaSession.setPlaybackState(this.playbackState.build());
    }

    private float getPlaybackSpeed(boolean z, MessageObject messageObject) {
        if (!z) {
            return 0.0f;
        }
        if (messageObject == null) {
            return 1.0f;
        }
        if (messageObject.isVoice() || messageObject.isRoundVideo()) {
            return MediaController.getInstance().getPlaybackSpeed(false);
        }
        return 1.0f;
    }

    public void setListeners(RemoteViews remoteViews) {
        remoteViews.setOnClickPendingIntent(R.id.player_previous, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PREVIOUS), fixIntentFlags(167772160)));
        remoteViews.setOnClickPendingIntent(R.id.player_close, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_CLOSE), fixIntentFlags(167772160)));
        remoteViews.setOnClickPendingIntent(R.id.player_pause, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PAUSE), fixIntentFlags(167772160)));
        remoteViews.setOnClickPendingIntent(R.id.player_next, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_NEXT), fixIntentFlags(167772160)));
        remoteViews.setOnClickPendingIntent(R.id.player_play, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PLAY), fixIntentFlags(167772160)));
    }

    private int fixIntentFlags(int i) {
        return (Build.VERSION.SDK_INT >= 31 || !XiaomiUtilities.isMIUI()) ? i : i & (-100663297);
    }

    @Override // android.app.Service
    @SuppressLint({"NewApi"})
    public void onDestroy() {
        unregisterReceiver(this.headsetPlugReceiver);
        super.onDestroy();
        stopForeground(true);
        RemoteControlClient remoteControlClient = this.remoteControlClient;
        if (remoteControlClient != null) {
            RemoteControlClient.MetadataEditor metadataEditorEditMetadata = remoteControlClient.editMetadata(true);
            metadataEditorEditMetadata.clear();
            metadataEditorEditMetadata.apply();
            this.audioManager.unregisterRemoteControlClient(this.remoteControlClient);
        }
        this.mediaSession.release();
        for (int i = 0; i < 16; i++) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidSeek);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.fileLoaded);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        String str;
        String str2;
        if (i == NotificationCenter.messagePlayingPlayStateChanged) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                createNotification(playingMessageObject, false);
                return;
            } else {
                stopSelf();
                return;
            }
        }
        if (i == NotificationCenter.messagePlayingDidSeek) {
            MessageObject playingMessageObject2 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject2 == null) {
                return;
            }
            long jRound = ((long) Math.round(playingMessageObject2.audioPlayerDuration * ((Float) objArr[1]).floatValue())) * 1000;
            updatePlaybackState(jRound);
            RemoteControlClient remoteControlClient = this.remoteControlClient;
            if (remoteControlClient != null) {
                remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3, jRound, MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
                return;
            }
            return;
        }
        if (i == NotificationCenter.httpFileDidLoad) {
            String str3 = (String) objArr[0];
            MessageObject playingMessageObject3 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject3 == null || (str2 = this.loadingFilePath) == null || !str2.equals(str3)) {
                return;
            }
            createNotification(playingMessageObject3, false);
            return;
        }
        if (i == NotificationCenter.fileLoaded) {
            String str4 = (String) objArr[0];
            MessageObject playingMessageObject4 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject4 == null || (str = this.loadingFilePath) == null || !str.equals(str4)) {
                return;
            }
            createNotification(playingMessageObject4, false);
        }
    }
}
