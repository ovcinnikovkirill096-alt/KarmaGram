package org.telegram.messenger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.utils.system.SystemUtils;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.common.images.WebImage;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.chromecast.ChromecastController;
import org.telegram.messenger.chromecast.ChromecastFileServer;
import org.telegram.messenger.chromecast.ChromecastMedia;
import org.telegram.messenger.chromecast.ChromecastMediaVariations;
import org.telegram.messenger.video.MediaCodecVideoConvertor;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.FiltersView;
import org.telegram.ui.CastSync;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.PermissionRequest;
import org.telegram.ui.Components.PhotoFilterView;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stories.DarkThemeResourceProvider;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.webrtc.MediaStreamTrack;

public class MediaController implements AudioManager.OnAudioFocusChangeListener, NotificationCenter.NotificationCenterDelegate, SensorEventListener {
    private static final int AUDIO_FOCUSED = 2;
    public static final String AUDIO_MIME_TYPE = "audio/mp4a-latm";
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static volatile MediaController Instance = null;
    public static final int VIDEO_BITRATE_1080 = 14000000;
    public static final int VIDEO_BITRATE_360 = 3000000;
    public static final int VIDEO_BITRATE_480 = 5000000;
    public static final int VIDEO_BITRATE_720 = 9000000;
    public static final String VIDEO_MIME_TYPE = "video/avc";
    private static final float VOLUME_DUCK = 0.2f;
    private static final float VOLUME_NORMAL = 1.0f;
    public static AlbumEntry allMediaAlbumEntry;
    public static ArrayList<AlbumEntry> allMediaAlbums;
    public static ArrayList<AlbumEntry> allPhotoAlbums;
    public static AlbumEntry allPhotosAlbumEntry;
    public static AlbumEntry allVideosAlbumEntry;
    private static Runnable broadcastPhotosRunnable;
    private static final ConcurrentHashMap<String, Integer> cachedEncoderBitrates;
    public static boolean forceBroadcastNewPhotos;
    private static final String[] projectionPhotos;
    private static final String[] projectionVideo;
    private static Runnable refreshGalleryRunnable;
    private static long volumeBarLastTimeShown;
    private Sensor accelerometerSensor;
    private boolean accelerometerVertical;
    private boolean allowStartRecord;
    private AudioInfo audioInfo;
    private AudioRecord audioRecorder;
    private boolean audioRecorderPaused;
    private float audioVolume;
    private ValueAnimator audioVolumeAnimator;
    private Activity baseActivity;
    private boolean callInProgress;
    private int countLess;
    private AspectRatioFrameLayout currentAspectRatioFrameLayout;
    private float currentAspectRatioFrameLayoutRatio;
    private boolean currentAspectRatioFrameLayoutReady;
    private int currentAspectRatioFrameLayoutRotation;
    private VideoConvertMessage currentForegroundConvertingVideo;
    private int currentPlaylistNum;
    public MessagesController.SavedMusicList currentSavedMusicList;
    private TextureView currentTextureView;
    private FrameLayout currentTextureViewContainer;
    private boolean downloadingCurrentMessage;
    private ExternalObserver externalObserver;
    private View feedbackView;
    private ByteBuffer fileBuffer;
    private DispatchQueue fileEncodingQueue;
    private BaseFragment flagSecureFragment;
    private boolean forceLoopCurrentPlaylist;
    private MessageObject goingToShowMessageObject;
    private Sensor gravitySensor;
    private int hasAudioFocus;
    private boolean hasRecordAudioFocus;
    private boolean ignoreOnPause;
    private boolean ignorePlayerUpdate;
    private boolean ignoreProximity;
    private boolean inputFieldHasText;
    private InternalObserver internalObserver;
    private boolean isDrawingWasReady;
    private boolean isStreamingCurrentAudio;
    private long lastAccelerometerDetected;
    private int lastChatAccount;
    private long lastChatEnterTime;
    private long lastChatLeaveTime;
    private ArrayList<Long> lastChatVisibleMessages;
    private long lastMediaCheckTime;
    private int lastMessageId;
    private long lastSaveTime;
    private TLRPC.EncryptedChat lastSecretChat;
    private TLRPC.User lastUser;
    private Sensor linearSensor;
    private boolean loadingPlaylist;
    private boolean manualRecording;
    private String[] mediaProjections;
    private PipRoundVideoView pipRoundVideoView;
    private int pipSwitchingState;
    private boolean playMusicAgain;
    private int playerNum;
    private boolean playerWasReady;
    private MessageObject playingMessageObject;
    private int playlistClassGuid;
    private PlaylistGlobalSearchParams playlistGlobalSearchParams;
    private long playlistMergeDialogId;
    private float previousAccValue;
    private boolean proximityHasDifferentValues;
    private Sensor proximitySensor;
    private boolean proximityTouched;
    private PowerManager.WakeLock proximityWakeLock;
    private ChatActivity raiseChat;
    private boolean raiseToEarRecord;
    private int raisedToBack;
    private int raisedToTop;
    private int raisedToTopSign;
    private long recordDialogId;
    private long recordMonoForumPeerId;
    private MessageSuggestionParams recordMonoForumSuggestionParams;
    private DispatchQueue recordQueue;
    private String recordQuickReplyShortcut;
    private int recordQuickReplyShortcutId;
    private MessageObject recordReplyingMsg;
    private TL_stories.StoryItem recordReplyingStory;
    private MessageObject recordReplyingTopMsg;
    private Runnable recordStartRunnable;
    private long recordStartTime;
    public long recordTimeCount;
    private long recordTopicId;
    public TLRPC.TL_document recordingAudio;
    private File recordingAudioFile;
    private int recordingCurrentAccount;
    private File recordingPrevAudioFile;
    private boolean resumeAudioOnFocusGain;
    public long samplesCount;
    private SavedMusicPlaylistState savedMusicPlaylistState;
    private float seekToProgressPending;
    private int sendAfterDone;
    private boolean sendAfterDoneNotify;
    private boolean sendAfterDoneOnce;
    private long sendAfterDonePayStars;
    private int sendAfterDoneScheduleDate;
    private SensorManager sensorManager;
    private boolean sensorsStarted;
    private String shouldSavePositionForCurrentAudio;
    private int startObserverToken;
    private StopMediaObserverRunnable stopMediaObserverRunnable;
    private long timeSinceRaise;
    private boolean useFrontSpeaker;
    private VideoPlayer videoPlayer;
    private ArrayList<MessageObject> voiceMessagesPlaylist;
    private SparseArray<MessageObject> voiceMessagesPlaylistMap;
    private boolean voiceMessagesPlaylistUnread;
    public int writtenFrame;
    AudioManager.OnAudioFocusChangeListener audioRecordFocusChangedListener = new AudioManager.OnAudioFocusChangeListener() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda44
        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public final void onAudioFocusChange(int i) {
            this.f$0.lambda$new$0(i);
        }
    };
    private final Object videoConvertSync = new Object();
    private long lastTimestamp = 0;
    private float lastProximityValue = -100.0f;
    private float[] gravity = new float[3];
    private float[] gravityFast = new float[3];
    private float[] linearAcceleration = new float[3];
    private int audioFocus = 0;
    private ArrayList<VideoConvertMessage> foregroundConvertingMessages = new ArrayList<>();
    private ArrayList<VideoConvertMessage> videoConvertQueue = new ArrayList<>();
    private final Object videoQueueSync = new Object();
    private HashMap<String, MessageObject> generatingWaveform = new HashMap<>();
    public boolean isSilent = false;
    private boolean isPaused = false;
    private boolean wasPlayingAudioBeforePause = false;
    private VideoPlayer audioPlayer = null;
    private VideoPlayer emojiSoundPlayer = null;
    private int emojiSoundPlayerNum = 0;
    private float currentPlaybackSpeed = 1.0f;
    private float currentMusicPlaybackSpeed = 1.0f;
    private float fastPlaybackSpeed = 1.0f;
    private float fastMusicPlaybackSpeed = 1.0f;
    private long lastProgress = 0;
    private java.util.Timer progressTimer = null;
    private final Object progressTimerSync = new Object();
    private ArrayList<MessageObject> playlist = new ArrayList<>();
    private HashMap<Integer, MessageObject> playlistMap = new HashMap<>();
    private ArrayList<MessageObject> shuffledPlaylist = new ArrayList<>();
    private boolean[] playlistEndReached = {false, false};
    private int[] playlistMaxId = {Integer.MAX_VALUE, Integer.MAX_VALUE};
    private Runnable setLoadingRunnable = new Runnable() { // from class: org.telegram.messenger.MediaController.1
        @Override // java.lang.Runnable
        public void run() {
            if (MediaController.this.playingMessageObject == null) {
                return;
            }
            FileLoader.getInstance(MediaController.this.playingMessageObject.currentAccount).setLoadingVideo(MediaController.this.playingMessageObject.getDocument(), true, false);
        }
    };
    private int recordingGuid = -1;
    public short[] recordSamples = new short[1024];
    private final Object sync = new Object();
    private ArrayList<ByteBuffer> recordBuffers = new ArrayList<>();
    public int recordBufferSize = 1280;
    public int sampleRate = 48000;
    private Runnable recordRunnable = new AnonymousClass2();
    private final ValueAnimator.AnimatorUpdateListener audioVolumeUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.messenger.MediaController.3
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            MediaController.this.audioVolume = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            MediaController.this.setPlayerVolume();
        }
    };

    public static class AudioEntry {
        public String author;
        public int duration;
        public String genre;
        public long id;
        public MessageObject messageObject;
        public String path;
        public String title;
    }

    public interface VideoConvertorListener {
        boolean checkConversionCanceled();

        void didWriteData(long j, float f);
    }

    public static native boolean cropOpusFile(String str, String str2, long j, long j2);

    private static int getVideoBitrateWithFactor(float f) {
        return (int) (f * 2000.0f * 1000.0f * 1.13f);
    }

    public static native byte[] getWaveform(String str);

    public static native int isOpusFile(String str);

    private static boolean isRecognizedFormat(int i) {
        if (i == 39 || i == 2130706688) {
            return true;
        }
        switch (i) {
            case 19:
            case 20:
            case 21:
                return true;
            default:
                return false;
        }
    }

    public static native boolean joinOpusFiles(String str, String str2, String str3);

    private native int startRecord(String str, int i);

    private native void stopRecord();

    /* JADX INFO: Access modifiers changed from: private */
    public native int writeFrame(ByteBuffer byteBuffer, int i);

    public native byte[] getWaveform2(short[] sArr, int i);

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public boolean isBuffering() {
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer != null) {
            return videoPlayer.isBuffering();
        }
        return false;
    }

    public VideoConvertMessage getCurrentForegroundConverMessage() {
        return this.currentForegroundConvertingVideo;
    }

    private static class AudioBuffer {
        ByteBuffer buffer;
        byte[] bufferBytes;
        int finished;
        long pcmOffset;
        int size;

        public AudioBuffer(int i) {
            this.buffer = ByteBuffer.allocateDirect(i);
            this.bufferBytes = new byte[i];
        }
    }

    static {
        int i = Build.VERSION.SDK_INT;
        projectionPhotos = new String[]{"_id", "bucket_id", "bucket_display_name", "_data", i > 28 ? "date_modified" : "datetaken", "orientation", "width", "height", "_size"};
        projectionVideo = new String[]{"_id", "bucket_id", "bucket_display_name", "_data", i > 28 ? "date_modified" : "datetaken", "duration", "width", "height", "_size"};
        cachedEncoderBitrates = new ConcurrentHashMap<>();
        allMediaAlbums = new ArrayList<>();
        allPhotoAlbums = new ArrayList<>();
    }

    public static class AlbumEntry {
        public int bucketId;
        public String bucketName;
        public PhotoEntry coverPhoto;
        public ArrayList<PhotoEntry> photos = new ArrayList<>();
        public SparseArray<PhotoEntry> photosByIds = new SparseArray<>();
        public boolean videoOnly;

        public AlbumEntry(int i, String str, PhotoEntry photoEntry) {
            this.bucketId = i;
            this.bucketName = str;
            this.coverPhoto = photoEntry;
        }

        public void addPhoto(PhotoEntry photoEntry) {
            this.photos.add(photoEntry);
            this.photosByIds.put(photoEntry.imageId, photoEntry);
        }
    }

    public static class SavedFilterState {
        public float blurAngle;
        public float blurExcludeBlurSize;
        public Point blurExcludePoint;
        public float blurExcludeSize;
        public int blurType;
        public float contrastValue;
        public PhotoFilterView.CurvesToolValue curvesToolValue = new PhotoFilterView.CurvesToolValue();
        public float enhanceValue;
        public float exposureValue;
        public float fadeValue;
        public float grainValue;
        public float highlightsValue;
        public float saturationValue;
        public float shadowsValue;
        public float sharpenValue;
        public float softenSkinValue;
        public int tintHighlightsColor;
        public int tintShadowsColor;
        public float vignetteValue;
        public float warmthValue;

        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeFloat(this.enhanceValue);
            outputSerializedData.writeFloat(this.softenSkinValue);
            outputSerializedData.writeFloat(this.exposureValue);
            outputSerializedData.writeFloat(this.contrastValue);
            outputSerializedData.writeFloat(this.warmthValue);
            outputSerializedData.writeFloat(this.saturationValue);
            outputSerializedData.writeFloat(this.fadeValue);
            outputSerializedData.writeInt32(this.tintShadowsColor);
            outputSerializedData.writeInt32(this.tintHighlightsColor);
            outputSerializedData.writeFloat(this.highlightsValue);
            outputSerializedData.writeFloat(this.shadowsValue);
            outputSerializedData.writeFloat(this.vignetteValue);
            outputSerializedData.writeFloat(this.grainValue);
            outputSerializedData.writeInt32(this.blurType);
            outputSerializedData.writeFloat(this.sharpenValue);
            this.curvesToolValue.serializeToStream(outputSerializedData);
            outputSerializedData.writeFloat(this.blurExcludeSize);
            if (this.blurExcludePoint == null) {
                outputSerializedData.writeInt32(1450380236);
            } else {
                outputSerializedData.writeInt32(-559038737);
                outputSerializedData.writeFloat(this.blurExcludePoint.x);
                outputSerializedData.writeFloat(this.blurExcludePoint.y);
            }
            outputSerializedData.writeFloat(this.blurExcludeBlurSize);
            outputSerializedData.writeFloat(this.blurAngle);
        }

        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.enhanceValue = inputSerializedData.readFloat(z);
            this.softenSkinValue = inputSerializedData.readFloat(z);
            this.exposureValue = inputSerializedData.readFloat(z);
            this.contrastValue = inputSerializedData.readFloat(z);
            this.warmthValue = inputSerializedData.readFloat(z);
            this.saturationValue = inputSerializedData.readFloat(z);
            this.fadeValue = inputSerializedData.readFloat(z);
            this.tintShadowsColor = inputSerializedData.readInt32(z);
            this.tintHighlightsColor = inputSerializedData.readInt32(z);
            this.highlightsValue = inputSerializedData.readFloat(z);
            this.shadowsValue = inputSerializedData.readFloat(z);
            this.vignetteValue = inputSerializedData.readFloat(z);
            this.grainValue = inputSerializedData.readFloat(z);
            this.blurType = inputSerializedData.readInt32(z);
            this.sharpenValue = inputSerializedData.readFloat(z);
            this.curvesToolValue.readParams(inputSerializedData, z);
            this.blurExcludeSize = inputSerializedData.readFloat(z);
            if (inputSerializedData.readInt32(z) == 1450380236) {
                this.blurExcludePoint = null;
            } else {
                if (this.blurExcludePoint == null) {
                    this.blurExcludePoint = new Point();
                }
                this.blurExcludePoint.x = inputSerializedData.readFloat(z);
                this.blurExcludePoint.y = inputSerializedData.readFloat(z);
            }
            this.blurExcludeBlurSize = inputSerializedData.readFloat(z);
            this.blurAngle = inputSerializedData.readFloat(z);
        }

        public boolean isEmpty() {
            return Math.abs(this.enhanceValue) < 0.1f && Math.abs(this.softenSkinValue) < 0.1f && Math.abs(this.exposureValue) < 0.1f && Math.abs(this.contrastValue) < 0.1f && Math.abs(this.warmthValue) < 0.1f && Math.abs(this.saturationValue) < 0.1f && Math.abs(this.fadeValue) < 0.1f && this.tintShadowsColor == 0 && this.tintHighlightsColor == 0 && Math.abs(this.highlightsValue) < 0.1f && Math.abs(this.shadowsValue) < 0.1f && Math.abs(this.vignetteValue) < 0.1f && Math.abs(this.grainValue) < 0.1f && this.blurType == 0 && Math.abs(this.sharpenValue) < 0.1f;
        }
    }

    public static class CropState extends TLObject {
        public static final int constructor = 1151577037;
        public float cropPx;
        public float cropPy;
        public float cropRotate;
        public boolean freeform;
        public int height;
        public boolean initied;
        public float lockedAspectRatio;
        public Matrix matrix;
        public boolean mirrored;
        public int orientation;
        public float scale;
        public float stateScale;
        public int transformHeight;
        public int transformRotation;
        public int transformWidth;
        public Matrix useMatrix;
        public int width;
        public float cropScale = 1.0f;
        public float cropPw = 1.0f;
        public float cropPh = 1.0f;

        public CropState clone() {
            CropState cropState = new CropState();
            cropState.cropPx = this.cropPx;
            cropState.cropPy = this.cropPy;
            cropState.cropScale = this.cropScale;
            cropState.cropRotate = this.cropRotate;
            cropState.cropPw = this.cropPw;
            cropState.cropPh = this.cropPh;
            cropState.transformWidth = this.transformWidth;
            cropState.transformHeight = this.transformHeight;
            cropState.transformRotation = this.transformRotation;
            cropState.mirrored = this.mirrored;
            cropState.stateScale = this.stateScale;
            cropState.scale = this.scale;
            cropState.matrix = this.matrix;
            cropState.width = this.width;
            cropState.height = this.height;
            cropState.freeform = this.freeform;
            cropState.lockedAspectRatio = this.lockedAspectRatio;
            cropState.orientation = this.orientation;
            cropState.initied = this.initied;
            cropState.useMatrix = this.useMatrix;
            return cropState;
        }

        public boolean isEmpty() {
            Matrix matrix = this.matrix;
            if (matrix != null && !matrix.isIdentity()) {
                return false;
            }
            Matrix matrix2 = this.useMatrix;
            return (matrix2 == null || matrix2.isIdentity()) && this.cropPw == 1.0f && this.cropPh == 1.0f && this.cropScale == 1.0f && this.cropRotate == 0.0f && this.transformWidth == 0 && this.transformHeight == 0 && this.transformRotation == 0 && !this.mirrored && this.stateScale == 0.0f && this.scale == 0.0f && this.width == 0 && this.height == 0 && !this.freeform && this.lockedAspectRatio == 0.0f;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.cropPx = inputSerializedData.readFloat(z);
            this.cropPy = inputSerializedData.readFloat(z);
            this.cropScale = inputSerializedData.readFloat(z);
            this.cropRotate = inputSerializedData.readFloat(z);
            this.cropPw = inputSerializedData.readFloat(z);
            this.cropPh = inputSerializedData.readFloat(z);
            this.transformWidth = inputSerializedData.readInt32(z);
            this.transformHeight = inputSerializedData.readInt32(z);
            this.transformRotation = inputSerializedData.readInt32(z);
            this.mirrored = inputSerializedData.readBool(z);
            this.stateScale = inputSerializedData.readFloat(z);
            this.scale = inputSerializedData.readFloat(z);
            float[] fArr = new float[9];
            for (int i = 0; i < 9; i++) {
                fArr[i] = inputSerializedData.readFloat(z);
            }
            Matrix matrix = new Matrix();
            this.matrix = matrix;
            matrix.setValues(fArr);
            this.width = inputSerializedData.readInt32(z);
            this.height = inputSerializedData.readInt32(z);
            this.freeform = inputSerializedData.readBool(z);
            this.lockedAspectRatio = inputSerializedData.readFloat(z);
            if (inputSerializedData.readInt32(z) == 178403937) {
                for (int i2 = 0; i2 < 9; i2++) {
                    fArr[i2] = inputSerializedData.readFloat(z);
                }
                Matrix matrix2 = new Matrix();
                this.useMatrix = matrix2;
                matrix2.setValues(fArr);
            }
            this.initied = inputSerializedData.readBool(z);
            this.orientation = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeFloat(this.cropPx);
            outputSerializedData.writeFloat(this.cropPy);
            outputSerializedData.writeFloat(this.cropScale);
            outputSerializedData.writeFloat(this.cropRotate);
            outputSerializedData.writeFloat(this.cropPw);
            outputSerializedData.writeFloat(this.cropPh);
            outputSerializedData.writeInt32(this.transformWidth);
            outputSerializedData.writeInt32(this.transformHeight);
            outputSerializedData.writeInt32(this.transformRotation);
            outputSerializedData.writeBool(this.mirrored);
            outputSerializedData.writeFloat(this.stateScale);
            outputSerializedData.writeFloat(this.scale);
            float[] fArr = new float[9];
            Matrix matrix = this.matrix;
            if (matrix != null) {
                matrix.getValues(fArr);
            } else {
                for (int i = 0; i < 9; i++) {
                    fArr[i] = 0.0f;
                }
            }
            for (int i2 = 0; i2 < 9; i2++) {
                outputSerializedData.writeFloat(fArr[i2]);
            }
            outputSerializedData.writeInt32(this.width);
            outputSerializedData.writeInt32(this.height);
            outputSerializedData.writeBool(this.freeform);
            outputSerializedData.writeFloat(this.lockedAspectRatio);
            if (this.useMatrix == null) {
                outputSerializedData.writeInt32(1450380236);
            } else {
                outputSerializedData.writeInt32(178403937);
                this.useMatrix.getValues(fArr);
                for (int i3 = 0; i3 < 9; i3++) {
                    outputSerializedData.writeFloat(fArr[i3]);
                }
            }
            outputSerializedData.writeBool(this.initied);
            outputSerializedData.writeInt32(this.orientation);
        }
    }

    public static class MediaEditState {
        public long averageDuration;
        public CharSequence caption;
        public String coverPath;
        public TLRPC.Photo coverPhoto;
        public Object coverPhotoParentObject;
        public long coverSavedPosition;
        public CropState cropState;
        public ArrayList<VideoEditedInfo.MediaEntity> croppedMediaEntities;
        public String croppedPaintPath;
        public VideoEditedInfo editedInfo;
        public long effectId;
        public ArrayList<TLRPC.MessageEntity> entities;
        public String filterPath;
        public String fullPaintPath;
        public boolean highQuality = ExteraConfig.alwaysSendInHD;
        public String imagePath;
        public boolean isCropped;
        public boolean isFiltered;
        public boolean isPainted;
        public ArrayList<VideoEditedInfo.MediaEntity> mediaEntities;
        public String paintPath;
        public SavedFilterState savedFilterState;
        public ArrayList<TLRPC.InputDocument> stickers;
        public String thumbPath;
        public int ttl;

        public String getPath() {
            return null;
        }

        public void reset() {
            this.caption = null;
            this.coverPath = null;
            this.coverPhoto = null;
            this.coverPhotoParentObject = null;
            this.thumbPath = null;
            this.filterPath = null;
            this.imagePath = null;
            this.paintPath = null;
            this.fullPaintPath = null;
            this.croppedPaintPath = null;
            this.isFiltered = false;
            this.isPainted = false;
            this.isCropped = false;
            this.ttl = 0;
            this.mediaEntities = null;
            this.editedInfo = null;
            this.entities = null;
            this.savedFilterState = null;
            this.stickers = null;
            this.cropState = null;
            this.highQuality = ExteraConfig.alwaysSendInHD;
        }

        public void resetEdit() {
            this.thumbPath = null;
            this.filterPath = null;
            this.imagePath = null;
            this.paintPath = null;
            this.croppedPaintPath = null;
            this.isFiltered = false;
            this.isPainted = false;
            this.isCropped = false;
            this.mediaEntities = null;
            this.editedInfo = null;
            this.entities = null;
            this.savedFilterState = null;
            this.stickers = null;
            this.cropState = null;
        }

        public void copyFrom(MediaEditState mediaEditState) {
            this.caption = mediaEditState.caption;
            this.thumbPath = mediaEditState.thumbPath;
            this.imagePath = mediaEditState.imagePath;
            this.filterPath = mediaEditState.filterPath;
            this.paintPath = mediaEditState.paintPath;
            this.croppedPaintPath = mediaEditState.croppedPaintPath;
            this.fullPaintPath = mediaEditState.fullPaintPath;
            this.entities = mediaEditState.entities;
            this.savedFilterState = mediaEditState.savedFilterState;
            this.mediaEntities = mediaEditState.mediaEntities;
            this.croppedMediaEntities = mediaEditState.croppedMediaEntities;
            this.stickers = mediaEditState.stickers;
            this.editedInfo = mediaEditState.editedInfo;
            this.averageDuration = mediaEditState.averageDuration;
            this.isFiltered = mediaEditState.isFiltered;
            this.isPainted = mediaEditState.isPainted;
            this.isCropped = mediaEditState.isCropped;
            this.ttl = mediaEditState.ttl;
            this.cropState = mediaEditState.cropState;
            this.coverPath = mediaEditState.coverPath;
            this.highQuality = mediaEditState.highQuality;
        }
    }

    public static class PhotoEntry extends MediaEditState {
        public int bucketId;
        public boolean canDeleteAfter;
        public long dateTaken;
        public int duration;
        public String emoji;
        public TLRPC.VideoSize emojiMarkup;
        public int gradientBottomColor;
        public int gradientTopColor;
        public boolean hasSpoiler;
        public int height;
        public int imageId;
        public int invert;
        public boolean isAttachSpoilerRevealed;
        public boolean isChatPreviewSpoilerRevealed;
        public boolean isMuted;
        public boolean isVideo;
        public int orientation;
        public String path;
        public long size;
        public long starsAmount;
        public BitmapDrawable thumb;
        public int videoOrientation = -1;
        public int width;

        public PhotoEntry(int i, int i2, long j, String str, int i3, boolean z, int i4, int i5, long j2) {
            this.bucketId = i;
            this.imageId = i2;
            this.dateTaken = j;
            this.path = str;
            this.width = i4;
            this.height = i5;
            this.size = j2;
            if (z) {
                this.duration = i3;
            } else {
                this.orientation = i3;
            }
            this.isVideo = z;
        }

        public PhotoEntry(int i, int i2, long j, String str, int i3, int i4, boolean z, int i5, int i6, long j2) {
            this.bucketId = i;
            this.imageId = i2;
            this.dateTaken = j;
            this.path = str;
            this.width = i5;
            this.height = i6;
            this.size = j2;
            this.duration = i4;
            this.orientation = i3;
            this.isVideo = z;
        }

        public PhotoEntry setOrientation(Pair<Integer, Integer> pair) {
            this.orientation = ((Integer) pair.first).intValue();
            this.invert = ((Integer) pair.second).intValue();
            return this;
        }

        public PhotoEntry setOrientation(int i, int i2) {
            this.orientation = i;
            this.invert = i2;
            return this;
        }

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public void copyFrom(MediaEditState mediaEditState) {
            super.copyFrom(mediaEditState);
            boolean z = mediaEditState instanceof PhotoEntry;
            this.hasSpoiler = z && ((PhotoEntry) mediaEditState).hasSpoiler;
            this.starsAmount = z ? ((PhotoEntry) mediaEditState).starsAmount : 0L;
        }

        public PhotoEntry clone() {
            PhotoEntry photoEntry = new PhotoEntry(this.bucketId, this.imageId, this.dateTaken, this.path, this.orientation, this.duration, this.isVideo, this.width, this.height, this.size);
            photoEntry.invert = this.invert;
            photoEntry.isMuted = this.isMuted;
            photoEntry.canDeleteAfter = this.canDeleteAfter;
            photoEntry.hasSpoiler = this.hasSpoiler;
            photoEntry.starsAmount = this.starsAmount;
            photoEntry.isChatPreviewSpoilerRevealed = this.isChatPreviewSpoilerRevealed;
            photoEntry.isAttachSpoilerRevealed = this.isAttachSpoilerRevealed;
            photoEntry.emojiMarkup = this.emojiMarkup;
            photoEntry.gradientTopColor = this.gradientTopColor;
            photoEntry.gradientBottomColor = this.gradientBottomColor;
            photoEntry.copyFrom(this);
            return photoEntry;
        }

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public String getPath() {
            return this.path;
        }

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public void reset() {
            if (this.isVideo && this.filterPath != null) {
                new File(this.filterPath).delete();
                this.filterPath = null;
            }
            this.hasSpoiler = false;
            this.starsAmount = 0L;
            super.reset();
        }

        public void deleteAll() {
            if (this.path != null) {
                try {
                    new File(this.path).delete();
                } catch (Exception unused) {
                }
            }
            if (this.fullPaintPath != null) {
                try {
                    new File(this.fullPaintPath).delete();
                } catch (Exception unused2) {
                }
            }
            if (this.paintPath != null) {
                try {
                    new File(this.paintPath).delete();
                } catch (Exception unused3) {
                }
            }
            if (this.imagePath != null) {
                try {
                    new File(this.imagePath).delete();
                } catch (Exception unused4) {
                }
            }
            if (this.filterPath != null) {
                try {
                    new File(this.filterPath).delete();
                } catch (Exception unused5) {
                }
            }
            if (this.croppedPaintPath != null) {
                try {
                    new File(this.croppedPaintPath).delete();
                } catch (Exception unused6) {
                }
            }
        }

        /* JADX WARN: Code duplicated, block: B:25:0x00ae  */
        /* JADX WARN: Code duplicated, block: B:27:0x00ba  */
        /* JADX WARN: Code duplicated, block: B:28:0x00bc  */
        /* JADX WARN: Code duplicated, block: B:30:0x00d8  */
        /* JADX WARN: Code duplicated, block: B:32:0x00dc  */
        /* JADX WARN: Code duplicated, block: B:33:0x00ea  */
        /* JADX WARN: Code duplicated, block: B:36:0x012f  */
        /* JADX WARN: Code duplicated, block: B:42:0x0141  */
        /* JADX WARN: Code duplicated, block: B:44:0x0146  */
        /* JADX WARN: Code duplicated, block: B:48:? A[RETURN, SYNTHETIC] */
        public void rebuildPhoto(boolean z) {
            Bitmap bitmap;
            Bitmap bitmapCreateBitmap;
            String str;
            Bitmap bitmapDecodeFile;
            int i;
            String str2 = this.filterPath;
            if (str2 == null) {
                str2 = this.path;
            }
            Pair<Integer, Integer> imageOrientation = AndroidUtilities.getImageOrientation(str2);
            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            Bitmap scaledBitmap = StoryEntry.getScaledBitmap(new StoryEntry.DecodeBitmap() { // from class: org.telegram.messenger.MediaController$PhotoEntry$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Stories.recorder.StoryEntry.DecodeBitmap
                public final Bitmap decode(BitmapFactory.Options options) {
                    return this.f$0.lambda$rebuildPhoto$0(options);
                }
            }, AndroidUtilities.getPhotoSize(z), AndroidUtilities.getPhotoSize(z), false, true);
            if (this.imagePath != null) {
                new File(this.imagePath).delete();
                this.imagePath = null;
            }
            CropState cropState = this.cropState;
            if (cropState != null) {
                bitmapCreateBitmap = PhotoViewer.createCroppedBitmap(scaledBitmap, cropState, new int[]{((Integer) imageOrientation.first).intValue(), ((Integer) imageOrientation.second).intValue()}, true);
                AndroidUtilities.recycleBitmap(scaledBitmap);
            } else {
                if (((Integer) imageOrientation.first).intValue() != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(((Integer) imageOrientation.first).intValue());
                    if (((Integer) imageOrientation.second).intValue() == 1) {
                        matrix.postScale(-1.0f, 1.0f);
                    } else if (((Integer) imageOrientation.second).intValue() == 2) {
                        matrix.postScale(1.0f, -1.0f);
                    }
                    bitmapCreateBitmap = Bitmaps.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    AndroidUtilities.recycleBitmap(scaledBitmap);
                } else {
                    bitmap = scaledBitmap;
                }
                str = this.fullPaintPath;
                if (str == null) {
                    float photoSize = AndroidUtilities.getPhotoSize(z);
                    float photoSize2 = AndroidUtilities.getPhotoSize(z);
                    if (z) {
                        i = 99;
                    } else {
                        i = 87;
                    }
                    this.imagePath = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(ImageLoader.scaleAndSaveImage(bitmap, compressFormat, photoSize, photoSize2, i, false, 101, 101), true).toString();
                } else {
                    if (this.cropState != null) {
                        Bitmap bitmapDecodeFile2 = BitmapFactory.decodeFile(str);
                        bitmapDecodeFile = PhotoViewer.createCroppedBitmap(bitmapDecodeFile2, this.cropState, null, false);
                        AndroidUtilities.recycleBitmap(bitmapDecodeFile2);
                    } else {
                        bitmapDecodeFile = BitmapFactory.decodeFile(str);
                    }
                    try {
                        Paint paint = new Paint(3);
                        Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmapCreateBitmap2);
                        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
                        canvas.scale(bitmap.getWidth() / bitmapDecodeFile.getWidth(), bitmap.getHeight() / bitmapDecodeFile.getHeight());
                        canvas.drawBitmap(bitmapDecodeFile, 0.0f, 0.0f, paint);
                        this.imagePath = PhotoViewer.getTempFileAbsolutePath();
                        bitmapCreateBitmap2.compress(compressFormat, z ? 99 : 87, new FileOutputStream(this.imagePath));
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (bitmapDecodeFile != null) {
                        bitmapDecodeFile.recycle();
                    }
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
            bitmap = bitmapCreateBitmap;
            str = this.fullPaintPath;
            if (str == null) {
                float photoSize3 = AndroidUtilities.getPhotoSize(z);
                float photoSize4 = AndroidUtilities.getPhotoSize(z);
                if (z) {
                    i = 99;
                } else {
                    i = 87;
                }
                this.imagePath = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(ImageLoader.scaleAndSaveImage(bitmap, compressFormat, photoSize3, photoSize4, i, false, 101, 101), true).toString();
            } else {
                if (this.cropState != null) {
                    Bitmap bitmapDecodeFile3 = BitmapFactory.decodeFile(str);
                    bitmapDecodeFile = PhotoViewer.createCroppedBitmap(bitmapDecodeFile3, this.cropState, null, false);
                    AndroidUtilities.recycleBitmap(bitmapDecodeFile3);
                } else {
                    bitmapDecodeFile = BitmapFactory.decodeFile(str);
                }
                Paint paint2 = new Paint(3);
                Bitmap bitmapCreateBitmap3 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmapCreateBitmap3);
                canvas2.drawBitmap(bitmap, 0.0f, 0.0f, paint2);
                canvas2.scale(bitmap.getWidth() / bitmapDecodeFile.getWidth(), bitmap.getHeight() / bitmapDecodeFile.getHeight());
                canvas2.drawBitmap(bitmapDecodeFile, 0.0f, 0.0f, paint2);
                this.imagePath = PhotoViewer.getTempFileAbsolutePath();
                bitmapCreateBitmap3.compress(compressFormat, z ? 99 : 87, new FileOutputStream(this.imagePath));
                if (bitmapDecodeFile != null) {
                    bitmapDecodeFile.recycle();
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Bitmap lambda$rebuildPhoto$0(BitmapFactory.Options options) {
            String str = this.filterPath;
            if (str == null) {
                str = this.path;
            }
            return BitmapFactory.decodeFile(str, options);
        }
    }

    public static class SearchImage extends MediaEditState {
        public CharSequence caption;
        public int date;
        public TLRPC.Document document;
        public int height;
        public String id;
        public String imageUrl;
        public TLRPC.BotInlineResult inlineResult;
        public HashMap<String, String> params;
        public TLRPC.Photo photo;
        public TLRPC.PhotoSize photoSize;
        public int size;
        public TLRPC.PhotoSize thumbPhotoSize;
        public String thumbUrl;
        public int type;
        public int width;

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public String getPath() {
            if (this.photoSize != null) {
                return FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.photoSize, true).getAbsolutePath();
            }
            if (this.document != null) {
                return FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.document, true).getAbsolutePath();
            }
            return ImageLoader.getHttpFilePath(this.imageUrl, "jpg").getAbsolutePath();
        }

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public void reset() {
            super.reset();
        }

        public String getAttachName() {
            TLRPC.PhotoSize photoSize = this.photoSize;
            if (photoSize != null) {
                return FileLoader.getAttachFileName(photoSize);
            }
            TLRPC.Document document = this.document;
            if (document != null) {
                return FileLoader.getAttachFileName(document);
            }
            return Utilities.MD5(this.imageUrl) + "." + ImageLoader.getHttpUrlExtension(this.imageUrl, "jpg");
        }

        public String getPathToAttach() {
            if (this.photoSize != null) {
                return FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.photoSize, true).getAbsolutePath();
            }
            if (this.document != null) {
                return FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.document, true).getAbsolutePath();
            }
            return this.imageUrl;
        }

        public SearchImage clone() {
            SearchImage searchImage = new SearchImage();
            searchImage.id = this.id;
            searchImage.imageUrl = this.imageUrl;
            searchImage.thumbUrl = this.thumbUrl;
            searchImage.width = this.width;
            searchImage.height = this.height;
            searchImage.size = this.size;
            searchImage.type = this.type;
            searchImage.date = this.date;
            searchImage.caption = this.caption;
            searchImage.document = this.document;
            searchImage.photo = this.photo;
            searchImage.photoSize = this.photoSize;
            searchImage.thumbPhotoSize = this.thumbPhotoSize;
            searchImage.inlineResult = this.inlineResult;
            searchImage.params = this.params;
            return searchImage;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        if (i != 1) {
            this.hasRecordAudioFocus = false;
        }
    }

    public static class VideoConvertMessage {
        public int currentAccount;
        public boolean foreground;
        public boolean foregroundConversion;
        public MessageObject messageObject;
        public VideoEditedInfo videoEditedInfo;

        public VideoConvertMessage(MessageObject messageObject, VideoEditedInfo videoEditedInfo, boolean z, boolean z2) {
            this.messageObject = messageObject;
            this.currentAccount = messageObject.currentAccount;
            this.videoEditedInfo = videoEditedInfo;
            this.foreground = z;
            this.foregroundConversion = z2;
        }
    }

    /* JADX INFO: renamed from: org.telegram.messenger.MediaController$2, reason: invalid class name */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ByteBuffer byteBufferAllocateDirect;
            if (MediaController.this.audioRecorder != null) {
                if (!MediaController.this.recordBuffers.isEmpty()) {
                    byteBufferAllocateDirect = (ByteBuffer) MediaController.this.recordBuffers.get(0);
                    MediaController.this.recordBuffers.remove(0);
                } else {
                    byteBufferAllocateDirect = ByteBuffer.allocateDirect(MediaController.this.recordBufferSize);
                    byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
                }
                final ByteBuffer byteBuffer = byteBufferAllocateDirect;
                byteBuffer.rewind();
                int i = MediaController.this.audioRecorder.read(byteBuffer, byteBuffer.capacity());
                if (i > 0) {
                    byteBuffer.limit(i);
                    double d = 0.0d;
                    try {
                        MediaController mediaController = MediaController.this;
                        long j = mediaController.samplesCount;
                        long j2 = ((long) (i / 2)) + j;
                        short[] sArr = mediaController.recordSamples;
                        int length = (int) ((j / j2) * ((double) sArr.length));
                        int length2 = sArr.length - length;
                        float f = 0.0f;
                        if (length != 0) {
                            float length3 = sArr.length / length;
                            float f2 = 0.0f;
                            for (int i2 = 0; i2 < length; i2++) {
                                short[] sArr2 = MediaController.this.recordSamples;
                                sArr2[i2] = sArr2[(int) f2];
                                f2 += length3;
                            }
                        }
                        float f3 = (i / 2.0f) / length2;
                        for (int i3 = 0; i3 < i / 2; i3++) {
                            short s = byteBuffer.getShort();
                            d += (double) (s * s);
                            if (i3 == ((int) f)) {
                                short[] sArr3 = MediaController.this.recordSamples;
                                if (length < sArr3.length) {
                                    sArr3[length] = s;
                                    f += f3;
                                    length++;
                                }
                            }
                        }
                        MediaController.this.samplesCount = j2;
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    byteBuffer.position(0);
                    final double dSqrt = Math.sqrt((d / ((double) i)) / 2.0d);
                    final boolean z = i != byteBuffer.capacity();
                    MediaController.this.fileEncodingQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$2$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$run$1(byteBuffer, z);
                        }
                    });
                    MediaController.this.recordQueue.postRunnable(MediaController.this.recordRunnable);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$2$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$run$2(dSqrt);
                        }
                    });
                    return;
                }
                MediaController.this.recordBuffers.add(byteBuffer);
                if (MediaController.this.sendAfterDone == 3 || MediaController.this.sendAfterDone == 4) {
                    return;
                }
                MediaController mediaController2 = MediaController.this;
                mediaController2.stopRecordingInternal(mediaController2.sendAfterDone, MediaController.this.sendAfterDoneNotify, MediaController.this.sendAfterDoneScheduleDate, MediaController.this.sendAfterDoneOnce, MediaController.this.sendAfterDonePayStars);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(final ByteBuffer byteBuffer, boolean z) {
            int iLimit;
            while (byteBuffer.hasRemaining()) {
                if (byteBuffer.remaining() > MediaController.this.fileBuffer.remaining()) {
                    iLimit = byteBuffer.limit();
                    byteBuffer.limit(MediaController.this.fileBuffer.remaining() + byteBuffer.position());
                } else {
                    iLimit = -1;
                }
                MediaController.this.fileBuffer.put(byteBuffer);
                if (MediaController.this.fileBuffer.position() == MediaController.this.fileBuffer.limit() || z) {
                    MediaController mediaController = MediaController.this;
                    if (mediaController.writeFrame(mediaController.fileBuffer, !z ? MediaController.this.fileBuffer.limit() : byteBuffer.position()) != 0) {
                        MediaController.this.fileBuffer.rewind();
                        MediaController mediaController2 = MediaController.this;
                        long j = mediaController2.recordTimeCount;
                        int iLimit2 = mediaController2.fileBuffer.limit() / 2;
                        MediaController mediaController3 = MediaController.this;
                        mediaController2.recordTimeCount = j + ((long) (iLimit2 / (mediaController3.sampleRate / MediaDataController.MAX_STYLE_RUNS_COUNT)));
                        mediaController3.writtenFrame++;
                    } else {
                        FileLog.e("writing frame failed");
                    }
                }
                if (iLimit != -1) {
                    byteBuffer.limit(iLimit);
                }
            }
            MediaController.this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$2$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$run$0(byteBuffer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(ByteBuffer byteBuffer) {
            MediaController.this.recordBuffers.add(byteBuffer);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$2(double d) {
            NotificationCenter.getInstance(MediaController.this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordProgressChanged, Integer.valueOf(MediaController.this.recordingGuid), Double.valueOf(d));
        }
    }

    private class InternalObserver extends ContentObserver {
        public InternalObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            MediaController.this.processMediaObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        }
    }

    private class ExternalObserver extends ContentObserver {
        public ExternalObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            MediaController.this.processMediaObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class GalleryObserverInternal extends ContentObserver {
        public GalleryObserverInternal() {
            super(null);
        }

        private void scheduleReloadRunnable() {
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.MediaController$GalleryObserverInternal$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleReloadRunnable$0();
                }
            };
            MediaController.refreshGalleryRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 2000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$scheduleReloadRunnable$0() {
            if (PhotoViewer.getInstance().isVisible()) {
                scheduleReloadRunnable();
            } else {
                MediaController.refreshGalleryRunnable = null;
                MediaController.loadGalleryPhotosAlbums(0);
            }
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            if (MediaController.refreshGalleryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(MediaController.refreshGalleryRunnable);
            }
            scheduleReloadRunnable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class GalleryObserverExternal extends ContentObserver {
        public GalleryObserverExternal() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            if (MediaController.refreshGalleryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(MediaController.refreshGalleryRunnable);
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.MediaController$GalleryObserverExternal$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.GalleryObserverExternal.$r8$lambda$RXdudxX90yFWybAMOEn80RyiCv0();
                }
            };
            MediaController.refreshGalleryRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 2000L);
        }

        public static /* synthetic */ void $r8$lambda$RXdudxX90yFWybAMOEn80RyiCv0() {
            MediaController.refreshGalleryRunnable = null;
            MediaController.loadGalleryPhotosAlbums(0);
        }
    }

    public static void checkGallery() {
        AlbumEntry albumEntry;
        if (Build.VERSION.SDK_INT < 24 || (albumEntry = allPhotosAlbumEntry) == null) {
            return;
        }
        final int size = albumEntry.photos.size();
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.$r8$lambda$I5n5O3fhxSGp8aHEIaMfpeoOmaI(size);
            }
        }, 2000L);
    }

    /* JADX WARN: Code duplicated, block: B:13:0x002c  */
    public static /* synthetic */ void $r8$lambda$I5n5O3fhxSGp8aHEIaMfpeoOmaI(int i) {
        Cursor cursorQuery;
        Cursor cursorQuery2;
        int i2;
        int i3;
        try {
            if (SystemUtils.isImagesPermissionGranted()) {
                cursorQuery = MediaStore.Images.Media.query(ApplicationLoader.applicationContext.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"COUNT(_id)"}, null, null, null);
                if (cursorQuery != null) {
                    try {
                        if (cursorQuery.moveToNext()) {
                            i3 = cursorQuery.getInt(0);
                        } else {
                            i3 = 0;
                        }
                    } catch (Throwable th) {
                        th = th;
                        try {
                            FileLog.e(th);
                            if (cursorQuery != null) {
                                cursorQuery.close();
                            }
                            cursorQuery2 = cursorQuery;
                            i2 = 0;
                        } catch (Throwable th2) {
                            if (cursorQuery != null) {
                                cursorQuery.close();
                                throw th2;
                            }
                            throw th2;
                        }
                    }
                } else {
                    i3 = 0;
                }
            } else {
                i3 = 0;
                cursorQuery = null;
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            cursorQuery2 = cursorQuery;
            i2 = i3;
        } catch (Throwable th3) {
            th = th3;
            cursorQuery = null;
        }
        try {
            if (SystemUtils.isVideoPermissionGranted() && (cursorQuery2 = MediaStore.Images.Media.query(ApplicationLoader.applicationContext.getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"COUNT(_id)"}, null, null, null)) != null && cursorQuery2.moveToNext()) {
                i2 += cursorQuery2.getInt(0);
            }
            if (cursorQuery2 != null) {
                cursorQuery2.close();
            }
        } catch (Throwable th4) {
            try {
                FileLog.e(th4);
                if (cursorQuery2 != null) {
                }
            } catch (Throwable th5) {
                if (cursorQuery2 != null) {
                    cursorQuery2.close();
                    throw th5;
                }
                throw th5;
            }
        }
        if (i != i2) {
            Runnable runnable = refreshGalleryRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                refreshGalleryRunnable = null;
            }
            loadGalleryPhotosAlbums(0);
        }
    }

    private final class StopMediaObserverRunnable implements Runnable {
        public int currentObserverToken;

        private StopMediaObserverRunnable() {
            this.currentObserverToken = 0;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.currentObserverToken == MediaController.this.startObserverToken) {
                try {
                    if (MediaController.this.internalObserver != null) {
                        ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver(MediaController.this.internalObserver);
                        MediaController.this.internalObserver = null;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                try {
                    if (MediaController.this.externalObserver != null) {
                        ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver(MediaController.this.externalObserver);
                        MediaController.this.externalObserver = null;
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        }
    }

    public static MediaController getInstance() {
        MediaController mediaController;
        MediaController mediaController2 = Instance;
        if (mediaController2 != null) {
            return mediaController2;
        }
        synchronized (MediaController.class) {
            try {
                mediaController = Instance;
                if (mediaController == null) {
                    mediaController = new MediaController();
                    Instance = mediaController;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return mediaController;
    }

    public MediaController() {
        DispatchQueue dispatchQueue = new DispatchQueue("recordQueue");
        this.recordQueue = dispatchQueue;
        dispatchQueue.setPriority(10);
        DispatchQueue dispatchQueue2 = new DispatchQueue("fileEncodingQueue");
        this.fileEncodingQueue = dispatchQueue2;
        dispatchQueue2.setPriority(10);
        this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$2();
            }
        });
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$3();
            }
        });
        this.fileBuffer = ByteBuffer.allocateDirect(1920);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$4();
            }
        });
        this.mediaProjections = new String[]{"_data", "_display_name", "bucket_display_name", Build.VERSION.SDK_INT > 28 ? "date_modified" : "datetaken", "title", "width", "height"};
        ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
        try {
            contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, new GalleryObserverExternal());
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            contentResolver.registerContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, true, new GalleryObserverInternal());
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, new GalleryObserverExternal());
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        try {
            contentResolver.registerContentObserver(MediaStore.Video.Media.INTERNAL_CONTENT_URI, true, new GalleryObserverInternal());
        } catch (Exception e4) {
            FileLog.e(e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        try {
            this.sampleRate = 48000;
            int minBufferSize = AudioRecord.getMinBufferSize(48000, 16, 2);
            if (minBufferSize <= 0) {
                minBufferSize = 1280;
            }
            this.recordBufferSize = minBufferSize;
            for (int i = 0; i < 5; i++) {
                ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(this.recordBufferSize);
                byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
                this.recordBuffers.add(byteBufferAllocateDirect);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        try {
            this.currentPlaybackSpeed = MessagesController.getGlobalMainSettings().getFloat("playbackSpeed", 1.0f);
            this.currentMusicPlaybackSpeed = MessagesController.getGlobalMainSettings().getFloat("musicPlaybackSpeed", 1.0f);
            this.fastPlaybackSpeed = MessagesController.getGlobalMainSettings().getFloat("fastPlaybackSpeed", 1.8f);
            this.fastMusicPlaybackSpeed = MessagesController.getGlobalMainSettings().getFloat("fastMusicPlaybackSpeed", 1.8f);
            SensorManager sensorManager = (SensorManager) ApplicationLoader.applicationContext.getSystemService("sensor");
            this.sensorManager = sensorManager;
            this.linearSensor = sensorManager.getDefaultSensor(10);
            Sensor defaultSensor = this.sensorManager.getDefaultSensor(9);
            this.gravitySensor = defaultSensor;
            if (this.linearSensor == null || defaultSensor == null) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("gravity or linear sensor not found");
                }
                this.accelerometerSensor = this.sensorManager.getDefaultSensor(1);
                this.linearSensor = null;
                this.gravitySensor = null;
            }
            this.proximitySensor = this.sensorManager.getDefaultSensor(8);
            this.proximityWakeLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(32, "telegram:proximity_lock");
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            AnonymousClass4 anonymousClass4 = new AnonymousClass4();
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            if (telephonyManager != null) {
                telephonyManager.listen(anonymousClass4, 32);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: renamed from: org.telegram.messenger.MediaController$4, reason: invalid class name */
    class AnonymousClass4 extends PhoneStateListener {
        AnonymousClass4() {
        }

        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(final int i, String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCallStateChanged$0(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCallStateChanged$0(int i) {
            if (i != 1) {
                if (i == 0) {
                    MediaController.this.callInProgress = false;
                    return;
                } else {
                    if (i == 2) {
                        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
                        if (embedBottomSheet != null) {
                            embedBottomSheet.pause();
                        }
                        MediaController.this.callInProgress = true;
                        return;
                    }
                    return;
                }
            }
            MediaController mediaController = MediaController.this;
            if (mediaController.isPlayingMessage(mediaController.playingMessageObject) && !MediaController.this.isMessagePaused()) {
                MediaController mediaController2 = MediaController.this;
                mediaController2.lambda$startAudioAgain$7(mediaController2.playingMessageObject);
            } else if (MediaController.this.recordStartRunnable != null || MediaController.this.recordingAudio != null) {
                MediaController.this.stopRecording(2, false, 0, false, 0L);
            }
            EmbedBottomSheet embedBottomSheet2 = EmbedBottomSheet.getInstance();
            if (embedBottomSheet2 != null) {
                embedBottomSheet2.pause();
            }
            MediaController.this.callInProgress = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        for (int i = 0; i < 16; i++) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.didReceiveNewMessages);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagesDeleted);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.removeAllMessagesFromDialog);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.musicDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.mediaDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.musicListLoaded);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.playerDidStartPlaying);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.stopAllHeavyOperations);
        }
    }

    @Override // android.media.AudioManager.OnAudioFocusChangeListener
    public void onAudioFocusChange(final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAudioFocusChange$5(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAudioFocusChange$5(int i) {
        if (i == -1) {
            if (isPlayingMessage(getPlayingMessageObject()) && !isMessagePaused()) {
                lambda$startAudioAgain$7(this.playingMessageObject);
            }
            this.hasAudioFocus = 0;
            this.audioFocus = 0;
        } else if (i == 1) {
            this.audioFocus = 2;
            if (this.resumeAudioOnFocusGain) {
                this.resumeAudioOnFocusGain = false;
                if (isPlayingMessage(getPlayingMessageObject()) && isMessagePaused()) {
                    playMessage(getPlayingMessageObject());
                }
            }
        } else if (i == -3) {
            this.audioFocus = 1;
        } else if (i == -2) {
            this.audioFocus = 0;
            if (isPlayingMessage(getPlayingMessageObject()) && !isMessagePaused()) {
                lambda$startAudioAgain$7(this.playingMessageObject);
                this.resumeAudioOnFocusGain = true;
            }
        }
        setPlayerVolume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlayerVolume() {
        float f;
        try {
            float f2 = 0.0f;
            if (this.isSilent) {
                f = 0.0f;
            } else {
                f = this.audioFocus != 1 ? 1.0f : VOLUME_DUCK;
            }
            VideoPlayer videoPlayer = this.audioPlayer;
            if (videoPlayer != null) {
                if (!CastSync.isActive()) {
                    f2 = this.audioVolume * f;
                }
                videoPlayer.setVolume(f2);
            } else {
                VideoPlayer videoPlayer2 = this.videoPlayer;
                if (videoPlayer2 != null) {
                    if (!CastSync.isActive()) {
                        f2 = f;
                    }
                    videoPlayer2.setVolume(f2);
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public VideoPlayer getVideoPlayer() {
        return this.videoPlayer;
    }

    private void startProgressTimer(MessageObject messageObject) {
        synchronized (this.progressTimerSync) {
            java.util.Timer timer = this.progressTimer;
            if (timer != null) {
                try {
                    timer.cancel();
                    this.progressTimer = null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
                messageObject.getFileName();
                java.util.Timer timer2 = new java.util.Timer();
                this.progressTimer = timer2;
                timer2.schedule(new AnonymousClass5(messageObject), 0L, 17L);
            } else {
                messageObject.getFileName();
                java.util.Timer timer3 = new java.util.Timer();
                this.progressTimer = timer3;
                timer3.schedule(new AnonymousClass5(messageObject), 0L, 17L);
            }
            throw th;
        }
    }

    /* JADX INFO: renamed from: org.telegram.messenger.MediaController$5, reason: invalid class name */
    class AnonymousClass5 extends TimerTask {
        final /* synthetic */ MessageObject val$currentPlayingMessageObject;

        AnonymousClass5(MessageObject messageObject) {
            this.val$currentPlayingMessageObject = messageObject;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            synchronized (MediaController.this.sync) {
                final MessageObject messageObject = this.val$currentPlayingMessageObject;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$5$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$run$1(messageObject);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(MessageObject messageObject) {
            long duration;
            long currentPosition;
            final float f;
            float bufferedPosition;
            if ((MediaController.this.audioPlayer == null && MediaController.this.videoPlayer == null) || MediaController.this.isPaused) {
                return;
            }
            try {
                if (MediaController.this.videoPlayer != null) {
                    duration = MediaController.this.videoPlayer.getDuration();
                    currentPosition = MediaController.this.videoPlayer.getCurrentPosition();
                    if (currentPosition >= 0 && duration > 0) {
                        float f2 = duration;
                        bufferedPosition = MediaController.this.videoPlayer.getBufferedPosition() / f2;
                        f = currentPosition / f2;
                        if (f >= 1.0f) {
                            return;
                        }
                    }
                    return;
                }
                duration = MediaController.this.audioPlayer.getDuration();
                currentPosition = MediaController.this.audioPlayer.getCurrentPosition();
                float f3 = duration >= 0 ? currentPosition / duration : 0.0f;
                float bufferedPosition2 = MediaController.this.audioPlayer.getBufferedPosition() / duration;
                if (duration != -9223372036854775807L && currentPosition >= 0 && MediaController.this.seekToProgressPending == 0.0f) {
                    f = f3;
                    bufferedPosition = bufferedPosition2;
                }
                return;
                MediaController.this.lastProgress = currentPosition;
                messageObject.audioPlayerDuration = (int) (duration / 1000);
                messageObject.audioProgress = f;
                messageObject.audioProgressSec = (int) (MediaController.this.lastProgress / 1000);
                messageObject.bufferedProgress = bufferedPosition;
                if (f >= 0.0f && MediaController.this.shouldSavePositionForCurrentAudio != null && SystemClock.elapsedRealtime() - MediaController.this.lastSaveTime >= 1000) {
                    final String str = MediaController.this.shouldSavePositionForCurrentAudio;
                    MediaController.this.lastSaveTime = SystemClock.elapsedRealtime();
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$5$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ApplicationLoader.applicationContext.getSharedPreferences("media_saved_pos", 0).edit().putFloat(str, f).apply();
                        }
                    });
                }
                NotificationCenter.getInstance(messageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(messageObject.getId()), Float.valueOf(f));
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private void stopProgressTimer() {
        synchronized (this.progressTimerSync) {
            java.util.Timer timer = this.progressTimer;
            if (timer != null) {
                try {
                    timer.cancel();
                    this.progressTimer = null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public void cleanup() {
        cleanupPlayer(true, true);
        this.audioInfo = null;
        this.playMusicAgain = false;
        for (int i = 0; i < 16; i++) {
            DownloadController.getInstance(i).cleanup();
        }
        this.videoConvertQueue.clear();
        this.generatingWaveform.clear();
        this.savedMusicPlaylistState = null;
        this.voiceMessagesPlaylist = null;
        this.voiceMessagesPlaylistMap = null;
        clearPlaylist();
        cancelVideoConvert(null);
    }

    private void clearPlaylist() {
        this.currentSavedMusicList = null;
        this.playlist.clear();
        this.playlistMap.clear();
        this.shuffledPlaylist.clear();
        this.playlistClassGuid = 0;
        boolean[] zArr = this.playlistEndReached;
        zArr[1] = false;
        zArr[0] = false;
        this.playlistMergeDialogId = 0L;
        int[] iArr = this.playlistMaxId;
        iArr[1] = Integer.MAX_VALUE;
        iArr[0] = Integer.MAX_VALUE;
        this.loadingPlaylist = false;
        this.playlistGlobalSearchParams = null;
        this.savedMusicPlaylistState = null;
    }

    public void startMediaObserver() {
        ApplicationLoader.applicationHandler.removeCallbacks(this.stopMediaObserverRunnable);
        this.startObserverToken++;
        try {
            if (this.internalObserver == null) {
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ExternalObserver externalObserver = new ExternalObserver();
                this.externalObserver = externalObserver;
                contentResolver.registerContentObserver(uri, false, externalObserver);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            if (this.externalObserver == null) {
                ContentResolver contentResolver2 = ApplicationLoader.applicationContext.getContentResolver();
                Uri uri2 = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                InternalObserver internalObserver = new InternalObserver();
                this.internalObserver = internalObserver;
                contentResolver2.registerContentObserver(uri2, false, internalObserver);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    public void stopMediaObserver() {
        if (this.stopMediaObserverRunnable == null) {
            this.stopMediaObserverRunnable = new StopMediaObserverRunnable();
        }
        this.stopMediaObserverRunnable.currentObserverToken = this.startObserverToken;
        ApplicationLoader.applicationHandler.postDelayed(this.stopMediaObserverRunnable, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMediaObserver(Uri uri) {
        Cursor cursorQuery = null;
        try {
            android.graphics.Point realScreenSize = AndroidUtilities.getRealScreenSize();
            cursorQuery = ApplicationLoader.applicationContext.getContentResolver().query(uri, this.mediaProjections, null, null, "date_added DESC LIMIT 1");
            final ArrayList arrayList = new ArrayList();
            if (cursorQuery != null) {
                while (cursorQuery.moveToNext()) {
                    String string = cursorQuery.getString(0);
                    String string2 = cursorQuery.getString(1);
                    String string3 = cursorQuery.getString(2);
                    long j = cursorQuery.getLong(3);
                    String string4 = cursorQuery.getString(4);
                    int i = cursorQuery.getInt(5);
                    int i2 = cursorQuery.getInt(6);
                    if (string == null || !string.toLowerCase().contains("screenshot")) {
                        if ((string2 == null || !string2.toLowerCase().contains("screenshot")) && ((string3 == null || !string3.toLowerCase().contains("screenshot")) && (string4 == null || !string4.toLowerCase().contains("screenshot")))) {
                        }
                    }
                    if (i == 0 || i2 == 0) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(string, options);
                        i = options.outWidth;
                        i2 = options.outHeight;
                    }
                    if (i > 0 && i2 > 0) {
                        try {
                            int i3 = realScreenSize.x;
                            if ((i != i3 || i2 != realScreenSize.y) && (i2 != i3 || i != realScreenSize.y)) {
                            }
                        } catch (Exception unused) {
                            arrayList.add(Long.valueOf(j));
                        }
                    }
                    arrayList.add(Long.valueOf(j));
                }
                cursorQuery.close();
            }
            if (!arrayList.isEmpty()) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda26
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$processMediaObserver$6(arrayList);
                    }
                });
            }
        } catch (Exception e) {
            FileLog.e(e);
        } finally {
            if (cursorQuery != null) {
                try {
                    cursorQuery.close();
                } catch (Exception unused2) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processMediaObserver$6(ArrayList arrayList) {
        NotificationCenter.getInstance(this.lastChatAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.screenshotTook, new Object[0]);
        checkScreenshots(arrayList);
    }

    private void checkScreenshots(ArrayList<Long> arrayList) {
        if (arrayList == null || arrayList.isEmpty() || this.lastChatEnterTime == 0) {
            return;
        }
        if (this.lastUser != null || (this.lastSecretChat instanceof TLRPC.TL_encryptedChat)) {
            boolean z = false;
            for (int i = 0; i < arrayList.size(); i++) {
                Long l = arrayList.get(i);
                if ((this.lastMediaCheckTime == 0 || l.longValue() > this.lastMediaCheckTime) && l.longValue() >= this.lastChatEnterTime && (this.lastChatLeaveTime == 0 || l.longValue() <= this.lastChatLeaveTime + 2000)) {
                    this.lastMediaCheckTime = Math.max(this.lastMediaCheckTime, l.longValue());
                    z = true;
                }
            }
            if (z) {
                if (this.lastSecretChat != null) {
                    SecretChatHelper.getInstance(this.lastChatAccount).sendScreenshotMessage(this.lastSecretChat, this.lastChatVisibleMessages, null);
                } else {
                    SendMessagesHelper.getInstance(this.lastChatAccount).sendScreenshotMessage(this.lastUser, this.lastMessageId, null);
                }
            }
        }
    }

    public void setLastVisibleMessageIds(int i, long j, long j2, TLRPC.User user, TLRPC.EncryptedChat encryptedChat, ArrayList<Long> arrayList, int i2) {
        this.lastChatEnterTime = j;
        this.lastChatLeaveTime = j2;
        this.lastChatAccount = i;
        this.lastSecretChat = encryptedChat;
        this.lastUser = user;
        this.lastMessageId = i2;
        this.lastChatVisibleMessages = arrayList;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        MessagesController.SavedMusicList savedMusicList;
        MessageObject messageObject;
        ArrayList<MessageObject> arrayList;
        int iIndexOf;
        int i3 = 0;
        if (i == NotificationCenter.fileLoaded || i == NotificationCenter.httpFileDidLoad) {
            String str = (String) objArr[0];
            MessageObject messageObject2 = this.playingMessageObject;
            if (messageObject2 != null && messageObject2.currentAccount == i2 && FileLoader.getAttachFileName(messageObject2.getDocument()).equals(str)) {
                if (this.downloadingCurrentMessage) {
                    this.playMusicAgain = true;
                    playMessage(this.playingMessageObject);
                    return;
                } else {
                    if (this.audioInfo == null) {
                        try {
                            this.audioInfo = AudioInfo.getAudioInfo(FileLoader.getInstance(UserConfig.selectedAccount).getPathToMessage(this.playingMessageObject.messageOwner));
                            return;
                        } catch (Exception e) {
                            FileLog.e(e);
                            return;
                        }
                    }
                    return;
                }
            }
            return;
        }
        if (i == NotificationCenter.messagesDeleted) {
            if (AyuConfig.saveDeletedMessages || ((Boolean) objArr[2]).booleanValue()) {
                return;
            }
            long jLongValue = ((Long) objArr[1]).longValue();
            ArrayList arrayList2 = (ArrayList) objArr[0];
            MessageObject messageObject3 = this.playingMessageObject;
            if (messageObject3 != null && jLongValue == messageObject3.messageOwner.peer_id.channel_id && arrayList2.contains(Integer.valueOf(messageObject3.getId()))) {
                cleanupPlayer(true, true);
            }
            ArrayList<MessageObject> arrayList3 = this.voiceMessagesPlaylist;
            if (arrayList3 == null || arrayList3.isEmpty() || jLongValue != this.voiceMessagesPlaylist.get(0).messageOwner.peer_id.channel_id) {
                return;
            }
            while (i3 < arrayList2.size()) {
                Integer num = (Integer) arrayList2.get(i3);
                MessageObject messageObject4 = this.voiceMessagesPlaylistMap.get(num.intValue());
                this.voiceMessagesPlaylistMap.remove(num.intValue());
                if (messageObject4 != null) {
                    this.voiceMessagesPlaylist.remove(messageObject4);
                }
                i3++;
            }
            return;
        }
        if (i == NotificationCenter.removeAllMessagesFromDialog) {
            if (AyuConfig.saveDeletedMessages) {
                return;
            }
            long jLongValue2 = ((Long) objArr[0]).longValue();
            MessageObject messageObject5 = this.playingMessageObject;
            if (messageObject5 == null || messageObject5.getDialogId() != jLongValue2) {
                return;
            }
            cleanupPlayer(false, true);
            return;
        }
        if (i == NotificationCenter.musicDidLoad) {
            long jLongValue3 = ((Long) objArr[0]).longValue();
            MessageObject messageObject6 = this.playingMessageObject;
            if (messageObject6 == null || !messageObject6.isMusic() || this.playingMessageObject.getDialogId() != jLongValue3 || this.playingMessageObject.scheduled) {
                return;
            }
            ArrayList arrayList4 = (ArrayList) objArr[1];
            ArrayList arrayList5 = (ArrayList) objArr[2];
            this.playlist.addAll(0, arrayList4);
            this.playlist.addAll(arrayList5);
            int size = this.playlist.size();
            for (int i4 = 0; i4 < size; i4++) {
                MessageObject messageObject7 = this.playlist.get(i4);
                this.playlistMap.put(Integer.valueOf(messageObject7.getId()), messageObject7);
                int[] iArr = this.playlistMaxId;
                iArr[0] = Math.min(iArr[0], messageObject7.getId());
            }
            sortPlaylist();
            if (SharedConfig.shuffleMusic) {
                buildShuffledPlayList();
            } else {
                MessageObject messageObject8 = this.playingMessageObject;
                if (messageObject8 != null && (iIndexOf = this.playlist.indexOf(messageObject8)) >= 0) {
                    this.currentPlaylistNum = iIndexOf;
                }
            }
            this.playlistClassGuid = ConnectionsManager.generateClassGuid();
            return;
        }
        if (i == NotificationCenter.mediaDidLoad) {
            if (((Integer) objArr[3]).intValue() != this.playlistClassGuid || this.playingMessageObject == null) {
                return;
            }
            long jLongValue4 = ((Long) objArr[0]).longValue();
            ((Integer) objArr[4]).getClass();
            ArrayList arrayList6 = (ArrayList) objArr[2];
            DialogObject.isEncryptedDialog(jLongValue4);
            char c = jLongValue4 == this.playlistMergeDialogId ? (char) 1 : (char) 0;
            if (!arrayList6.isEmpty()) {
                this.playlistEndReached[c] = ((Boolean) objArr[5]).booleanValue();
            }
            int i5 = 0;
            for (int i6 = 0; i6 < arrayList6.size(); i6++) {
                MessageObject messageObject9 = (MessageObject) arrayList6.get(i6);
                if (!messageObject9.isVoiceOnce() && !this.playlistMap.containsKey(Integer.valueOf(messageObject9.getId()))) {
                    i5++;
                    this.playlist.add(0, messageObject9);
                    this.playlistMap.put(Integer.valueOf(messageObject9.getId()), messageObject9);
                    int[] iArr2 = this.playlistMaxId;
                    iArr2[c] = Math.min(iArr2[c], messageObject9.getId());
                }
            }
            sortPlaylist();
            int iIndexOf2 = this.playlist.indexOf(this.playingMessageObject);
            if (iIndexOf2 >= 0) {
                this.currentPlaylistNum = iIndexOf2;
            }
            this.loadingPlaylist = false;
            if (SharedConfig.shuffleMusic) {
                buildShuffledPlayList();
            }
            if (i5 != 0) {
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.moreMusicDidLoad, Integer.valueOf(i5));
                return;
            }
            return;
        }
        if (i == NotificationCenter.didReceiveNewMessages) {
            if (((Boolean) objArr[2]).booleanValue() || (arrayList = this.voiceMessagesPlaylist) == null || arrayList.isEmpty() || ((Long) objArr[0]).longValue() != this.voiceMessagesPlaylist.get(0).getDialogId()) {
                return;
            }
            ArrayList arrayList7 = (ArrayList) objArr[1];
            while (i3 < arrayList7.size()) {
                MessageObject messageObject10 = (MessageObject) arrayList7.get(i3);
                if ((messageObject10.isVoice() || messageObject10.isRoundVideo()) && !messageObject10.isVoiceOnce() && !messageObject10.isRoundOnce() && (!this.voiceMessagesPlaylistUnread || (messageObject10.isContentUnread() && !messageObject10.isOut()))) {
                    this.voiceMessagesPlaylist.add(messageObject10);
                    this.voiceMessagesPlaylistMap.put(messageObject10.getId(), messageObject10);
                }
                i3++;
            }
            return;
        }
        if (i == NotificationCenter.playerDidStartPlaying) {
            if (!isCurrentPlayer((VideoPlayer) objArr[0])) {
                MessageObject playingMessageObject = getPlayingMessageObject();
                if (playingMessageObject != null && isPlayingMessage(playingMessageObject) && !isMessagePaused() && (playingMessageObject.isMusic() || playingMessageObject.isVoice())) {
                    this.wasPlayingAudioBeforePause = true;
                }
                lambda$startAudioAgain$7(playingMessageObject);
                return;
            }
            if (LaunchActivity.isResumed) {
                return;
            }
            pauseInBackgroundIfNeeded();
            return;
        }
        if (i == NotificationCenter.stopAllHeavyOperations) {
            if (((Integer) objArr[0]).intValue() != 4096 || LaunchActivity.isResumed) {
                return;
            }
            pauseInBackgroundIfNeeded();
            return;
        }
        if (i == NotificationCenter.musicListLoaded && (savedMusicList = this.currentSavedMusicList) != null && objArr[0] == savedMusicList) {
            int size2 = savedMusicList.list.size() - this.playlist.size();
            this.playlist.clear();
            this.playlist.addAll(this.currentSavedMusicList.list);
            sortPlaylist();
            if (SharedConfig.shuffleMusic) {
                buildShuffledPlayList();
            } else {
                MessageObject messageObject11 = this.playingMessageObject;
                if (messageObject11 != null) {
                    int iIndexOf3 = this.playlist.indexOf(messageObject11);
                    if (iIndexOf3 >= 0) {
                        this.currentPlaylistNum = iIndexOf3;
                    } else {
                        int i7 = this.currentPlaylistNum;
                        if (i7 < 0 || i7 >= this.playlist.size()) {
                            this.currentPlaylistNum = 0;
                        }
                        if (this.playlist.size() == 0) {
                            cleanup();
                        } else {
                            playMessage(this.playlist.get(0));
                        }
                    }
                }
            }
            if (size2 == 0 || (messageObject = this.playingMessageObject) == null) {
                return;
            }
            NotificationCenter.getInstance(messageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.moreMusicDidLoad, Integer.valueOf(size2));
        }
    }

    private void pauseInBackgroundIfNeeded() {
        MessageObject playingMessageObject = getPlayingMessageObject();
        if (playingMessageObject == null) {
            return;
        }
        if (((playingMessageObject.isVoice() && ExteraConfig.pauseOnMinimizeVoice) || (playingMessageObject.isRoundVideo() && ExteraConfig.pauseOnMinimizeRound)) && isPlayingMessage(playingMessageObject) && !isMessagePaused()) {
            lambda$startAudioAgain$7(playingMessageObject);
        }
    }

    protected boolean isRecordingAudio() {
        return (this.recordStartRunnable == null && this.recordingAudio == null) ? false : true;
    }

    private boolean isNearToSensor(float f) {
        return f < 5.0f && f != this.proximitySensor.getMaximumRange();
    }

    public boolean isRecordingOrListeningByProximity() {
        if (!this.proximityTouched) {
            return false;
        }
        if (isRecordingAudio()) {
            return true;
        }
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject != null) {
            return messageObject.isVoice() || this.playingMessageObject.isRoundVideo();
        }
        return false;
    }

    private boolean forbidRaiseToListen() {
        try {
            for (AudioDeviceInfo audioDeviceInfo : NotificationsController.audioManager.getDevices(2)) {
                int type = audioDeviceInfo.getType();
                if ((type == 8 || type == 7 || type == 26 || type == 27 || type == 4 || type == 3) && audioDeviceInfo.isSink()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r18v1 */
    /* JADX WARN: Type inference failed for: r18v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r18v3 */
    /* JADX WARN: Type inference failed for: r18v4 */
    /* JADX WARN: Type inference failed for: r18v5 */
    /* JADX WARN: Type inference failed for: r18v6 */
    /* JADX WARN: Type inference failed for: r19v0, types: [org.telegram.messenger.MediaController] */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v26 */
    /* JADX WARN: Type inference failed for: r1v27 */
    /* JADX WARN: Type inference failed for: r1v64, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v73 */
    /* JADX WARN: Type inference failed for: r1v8, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v30 */
    /* JADX WARN: Type inference failed for: r2v31 */
    /* JADX WARN: Type inference failed for: r2v38 */
    /* JADX WARN: Type inference failed for: r4v24 */
    /* JADX WARN: Type inference failed for: r4v25 */
    /* JADX WARN: Type inference failed for: r4v26 */
    /* JADX WARN: Type inference failed for: r4v27 */
    /* JADX WARN: Type inference failed for: r4v39 */
    /* JADX WARN: Type inference failed for: r4v40 */
    /* JADX WARN: Type inference failed for: r4v41 */
    /* JADX WARN: Type inference failed for: r4v48 */
    /* JADX WARN: Type inference failed for: r4v49 */
    /* JADX WARN: Type inference failed for: r5v11 */
    /* JADX WARN: Type inference failed for: r5v12, types: [int] */
    /* JADX WARN: Type inference failed for: r5v14 */
    /* JADX WARN: Type inference failed for: r5v16 */
    /* JADX WARN: Type inference failed for: r5v17, types: [boolean] */
    /* JADX WARN: Type inference failed for: r5v22 */
    /* JADX WARN: Type inference failed for: r6v14 */
    /* JADX WARN: Type inference failed for: r6v15, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v17 */
    /* JADX WARN: Type inference failed for: r7v10 */
    /* JADX WARN: Type inference failed for: r7v8 */
    /* JADX WARN: Type inference failed for: r7v9 */
    /* JADX WARN: Type inference failed for: r9v5, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r9v7, types: [java.lang.StringBuilder] */
    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        long j;
        boolean z;
        char c;
        ?? r18;
        ?? r5;
        ?? r4;
        MessageObject messageObject;
        if (this.sensorsStarted && VoIPService.getSharedInstance() == null) {
            if (sensorEvent.sensor.getType() == 8) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("proximity changed to " + sensorEvent.values[0] + " max value = " + sensorEvent.sensor.getMaximumRange());
                }
                float f = this.lastProximityValue;
                float f2 = sensorEvent.values[0];
                if (f != f2) {
                    this.proximityHasDifferentValues = true;
                }
                this.lastProximityValue = f2;
                if (this.proximityHasDifferentValues) {
                    this.proximityTouched = isNearToSensor(f2);
                }
                j = 0;
                r18 = 1;
                c = 2;
            } else {
                Sensor sensor = sensorEvent.sensor;
                if (sensor == this.accelerometerSensor) {
                    long j2 = this.lastTimestamp;
                    double d = j2 == 0 ? 0.9800000190734863d : 1.0d / (((sensorEvent.timestamp - j2) / 1.0E9d) + 1.0d);
                    this.lastTimestamp = sensorEvent.timestamp;
                    float[] fArr = this.gravity;
                    double d2 = ((double) fArr[0]) * d;
                    double d3 = 1.0d - d;
                    float[] fArr2 = sensorEvent.values;
                    j = 0;
                    float f3 = (float) (d2 + (((double) fArr2[0]) * d3));
                    fArr[0] = f3;
                    r18 = 1;
                    c = 2;
                    float f4 = (float) ((((double) fArr[1]) * d) + (((double) fArr2[1]) * d3));
                    fArr[1] = f4;
                    float f5 = (float) ((d * ((double) fArr[2])) + (d3 * ((double) fArr2[2])));
                    fArr[2] = f5;
                    float[] fArr3 = this.gravityFast;
                    fArr3[0] = (f3 * 0.8f) + (fArr2[0] * 0.19999999f);
                    fArr3[1] = (f4 * 0.8f) + (fArr2[1] * 0.19999999f);
                    fArr3[2] = (f5 * 0.8f) + (fArr2[2] * 0.19999999f);
                    float[] fArr4 = this.linearAcceleration;
                    fArr4[0] = fArr2[0] - fArr[0];
                    fArr4[1] = fArr2[1] - fArr[1];
                    fArr4[2] = fArr2[2] - fArr[2];
                } else {
                    j = 0;
                    z = true;
                    c = 2;
                    if (sensor == this.linearSensor) {
                        float[] fArr5 = this.linearAcceleration;
                        float[] fArr6 = sensorEvent.values;
                        fArr5[0] = fArr6[0];
                        fArr5[1] = fArr6[1];
                        fArr5[2] = fArr6[2];
                        r18 = z;
                    } else if (sensor == this.gravitySensor) {
                        r18 = z;
                        float[] fArr7 = this.gravityFast;
                        float[] fArr8 = this.gravity;
                        float[] fArr9 = sensorEvent.values;
                        float f6 = fArr9[0];
                        fArr8[0] = f6;
                        fArr7[0] = f6;
                        float f7 = fArr9[1];
                        fArr8[1] = f7;
                        fArr7[1] = f7;
                        float f8 = fArr9[2];
                        fArr8[2] = f8;
                        fArr7[2] = f8;
                        r18 = z;
                    }
                }
            }
            r18 = z;
            Sensor sensor2 = sensorEvent.sensor;
            if (sensor2 == this.linearSensor || sensor2 == this.gravitySensor || sensor2 == this.accelerometerSensor) {
                float[] fArr10 = this.gravity;
                float f9 = fArr10[0];
                float[] fArr11 = this.linearAcceleration;
                float f10 = (f9 * fArr11[0]) + (fArr10[r18] * fArr11[r18]) + (fArr10[c] * fArr11[c]);
                int i = this.raisedToBack;
                if (i != 6 && ((f10 > 0.0f && this.previousAccValue > 0.0f) || (f10 < 0.0f && this.previousAccValue < 0.0f))) {
                    if (f10 > 0.0f) {
                        r5 = r18;
                        r4 = f10 > 15.0f ? r18 : 0;
                    } else {
                        r5 = c;
                        r4 = f10 < -15.0f ? r18 : 0;
                    }
                    int i2 = this.raisedToTopSign;
                    if (i2 != 0 && i2 != r5) {
                        int i3 = this.raisedToTop;
                        if (i3 != 6 || r4 == 0) {
                            if (r4 == 0) {
                                this.countLess++;
                            }
                            if (this.countLess == 10 || i3 != 6 || i != 0) {
                                this.raisedToTop = 0;
                                this.raisedToTopSign = 0;
                                this.raisedToBack = 0;
                                this.countLess = 0;
                            }
                        } else if (i < 6) {
                            int i4 = i + 1;
                            this.raisedToBack = i4;
                            if (i4 == 6) {
                                this.raisedToTop = 0;
                                this.raisedToTopSign = 0;
                                this.countLess = 0;
                                this.timeSinceRaise = System.currentTimeMillis();
                                if (BuildVars.LOGS_ENABLED && BuildVars.DEBUG_PRIVATE_VERSION) {
                                    FileLog.d("motion detected");
                                }
                            }
                        }
                    } else if (r4 != 0 && i == 0 && (i2 == 0 || i2 == r5)) {
                        int i5 = this.raisedToTop;
                        if (i5 < 6 && !this.proximityTouched) {
                            this.raisedToTopSign = r5;
                            int i6 = i5 + 1;
                            this.raisedToTop = i6;
                            if (i6 == 6) {
                                this.countLess = 0;
                            }
                        }
                    } else {
                        if (r4 == 0) {
                            this.countLess++;
                        }
                        if (i2 != r5 || this.countLess == 10 || this.raisedToTop != 6 || i != 0) {
                            this.raisedToBack = 0;
                            this.raisedToTop = 0;
                            this.raisedToTopSign = 0;
                            this.countLess = 0;
                        }
                    }
                }
                this.previousAccValue = f10;
                float[] fArr12 = this.gravityFast;
                this.accelerometerVertical = (fArr12[r18] <= 2.5f || Math.abs(fArr12[c]) >= 4.0f || Math.abs(this.gravityFast[0]) <= 1.5f) ? 0 : r18;
            }
            if (this.raisedToBack == 6 || this.accelerometerVertical) {
                this.lastAccelerometerDetected = System.currentTimeMillis();
            }
            ?? r1 = (this.manualRecording || this.playingMessageObject != null || !SharedConfig.enabledRaiseTo(r18) || !ApplicationLoader.isScreenOn || this.inputFieldHasText || !this.allowStartRecord || this.raiseChat == null || this.callInProgress) ? 0 : r18;
            ?? r2 = (SharedConfig.enabledRaiseTo(false) && (messageObject = this.playingMessageObject) != null && (messageObject.isVoice() || this.playingMessageObject.isRoundVideo())) ? r18 : 0;
            boolean z2 = this.proximityTouched;
            ?? r6 = (this.raisedToBack == 6 || this.accelerometerVertical || System.currentTimeMillis() - this.lastAccelerometerDetected < 60) ? r18 : 0;
            ?? r7 = (this.useFrontSpeaker || this.raiseToEarRecord) ? r18 : 0;
            ?? r8 = ((r6 == 0 && r7 == 0) || forbidRaiseToListen() || VoIPService.isAnyKindOfCallActive() || (r1 == 0 && r2 == 0) || PhotoViewer.getInstance().isVisible()) ? 0 : r18;
            PowerManager.WakeLock wakeLock = this.proximityWakeLock;
            if (wakeLock != null) {
                boolean zIsHeld = wakeLock.isHeld();
                if (zIsHeld && r8 == 0) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("wake lock releasing (proximityDetected=" + z2 + ", accelerometerDetected=" + r6 + ", alreadyPlaying=" + r7 + ")");
                    }
                    this.proximityWakeLock.release();
                } else if (!zIsHeld && r8 != 0) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("wake lock acquiring (proximityDetected=" + z2 + ", accelerometerDetected=" + r6 + ", alreadyPlaying=" + r7 + ")");
                    }
                    this.proximityWakeLock.acquire();
                }
            }
            boolean z3 = this.proximityTouched;
            if (z3 && r8 != 0) {
                if (r1 != 0 && this.recordStartRunnable == null) {
                    if (!this.raiseToEarRecord) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("start record");
                        }
                        ?? r3 = r18;
                        this.useFrontSpeaker = r3;
                        if (this.recordingAudio != null || !this.raiseChat.playFirstUnreadVoiceMessage()) {
                            this.raiseToEarRecord = r3;
                            this.useFrontSpeaker = false;
                            raiseToSpeakUpdated(r3);
                        }
                        if (this.useFrontSpeaker) {
                            setUseFrontSpeaker(r3);
                        }
                    }
                } else if (r2 != 0 && !this.useFrontSpeaker) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("start listen");
                    }
                    setUseFrontSpeaker(true);
                    startAudioAgain(false);
                }
                this.raisedToBack = 0;
                this.raisedToTop = 0;
                this.raisedToTopSign = 0;
                this.countLess = 0;
            } else if (z3 && ((this.accelerometerSensor == null || this.linearSensor == null) && this.gravitySensor == null && !VoIPService.isAnyKindOfCallActive())) {
                if (this.playingMessageObject != null && !ApplicationLoader.mainInterfacePaused && r2 != 0 && !this.useFrontSpeaker && !this.manualRecording && !forbidRaiseToListen()) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("start listen by proximity only");
                    }
                    setUseFrontSpeaker(true);
                    startAudioAgain(false);
                }
            } else if (!this.proximityTouched && !this.manualRecording) {
                if (this.raiseToEarRecord) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("stop record");
                    }
                    raiseToSpeakUpdated(false);
                    this.raiseToEarRecord = false;
                    this.ignoreOnPause = false;
                } else if (this.useFrontSpeaker) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("stop listen");
                    }
                    this.useFrontSpeaker = false;
                    startAudioAgain(true);
                    this.ignoreOnPause = false;
                }
            }
            if (this.timeSinceRaise == j || this.raisedToBack != 6 || Math.abs(System.currentTimeMillis() - this.timeSinceRaise) <= 1000) {
                return;
            }
            this.raisedToBack = 0;
            this.raisedToTop = 0;
            this.raisedToTopSign = 0;
            this.countLess = 0;
            this.timeSinceRaise = j;
        }
    }

    private void raiseToSpeakUpdated(boolean z) {
        if (this.recordingAudio != null) {
            toggleRecordingPause(false);
            return;
        }
        if (z) {
            int currentAccount = this.raiseChat.getCurrentAccount();
            long dialogId = this.raiseChat.getDialogId();
            MessageObject threadMessage = this.raiseChat.getThreadMessage();
            int classGuid = this.raiseChat.getClassGuid();
            ChatActivity chatActivity = this.raiseChat;
            String str = chatActivity != null ? chatActivity.quickReplyShortcut : null;
            int quickReplyId = chatActivity != null ? chatActivity.getQuickReplyId() : 0;
            ChatActivity chatActivity2 = this.raiseChat;
            long sendMonoForumPeerId = chatActivity2 != null ? chatActivity2.getSendMonoForumPeerId() : 0L;
            ChatActivity chatActivity3 = this.raiseChat;
            startRecording(currentAccount, dialogId, null, threadMessage, null, classGuid, false, str, quickReplyId, sendMonoForumPeerId, chatActivity3 != null ? chatActivity3.getSendMessageSuggestionParams() : null);
            return;
        }
        stopRecording(2, false, 0, false, 0L);
    }

    private void setUseFrontSpeaker(boolean z) {
        this.useFrontSpeaker = z;
        AudioManager audioManager = NotificationsController.audioManager;
        if (z) {
            audioManager.setBluetoothScoOn(false);
            audioManager.setSpeakerphoneOn(false);
        } else {
            audioManager.setSpeakerphoneOn(true);
        }
    }

    public void startRecordingIfFromSpeaker() {
        if (this.useFrontSpeaker && this.raiseChat != null && this.allowStartRecord && SharedConfig.enabledRaiseTo(true)) {
            this.raiseToEarRecord = true;
            int currentAccount = this.raiseChat.getCurrentAccount();
            long dialogId = this.raiseChat.getDialogId();
            MessageObject threadMessage = this.raiseChat.getThreadMessage();
            int classGuid = this.raiseChat.getClassGuid();
            ChatActivity chatActivity = this.raiseChat;
            String str = chatActivity != null ? chatActivity.quickReplyShortcut : null;
            int quickReplyId = chatActivity != null ? chatActivity.getQuickReplyId() : 0;
            ChatActivity chatActivity2 = this.raiseChat;
            long sendMonoForumPeerId = chatActivity2 != null ? chatActivity2.getSendMonoForumPeerId() : 0L;
            ChatActivity chatActivity3 = this.raiseChat;
            startRecording(currentAccount, dialogId, null, threadMessage, null, classGuid, false, str, quickReplyId, sendMonoForumPeerId, chatActivity3 != null ? chatActivity3.getSendMessageSuggestionParams() : null);
            this.ignoreOnPause = true;
        }
    }

    private void startAudioAgain(boolean z) {
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject == null) {
            return;
        }
        NotificationCenter.getInstance(messageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.audioRouteChanged, Boolean.valueOf(this.useFrontSpeaker));
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.setStreamType(this.useFrontSpeaker ? 0 : 3);
            if (!z) {
                if (this.videoPlayer.getCurrentPosition() < 1000) {
                    this.videoPlayer.seekTo(0L);
                }
                this.videoPlayer.play();
                return;
            }
            lambda$startAudioAgain$7(this.playingMessageObject);
            return;
        }
        VideoPlayer videoPlayer2 = this.audioPlayer;
        boolean z2 = videoPlayer2 != null;
        final MessageObject messageObject2 = this.playingMessageObject;
        float f = messageObject2.audioProgress;
        int i = messageObject2.audioPlayerDuration;
        if (z || videoPlayer2 == null || !videoPlayer2.isPlaying() || i * f > 1.0f) {
            messageObject2.audioProgress = f;
        } else {
            messageObject2.audioProgress = 0.0f;
        }
        cleanupPlayer(false, true);
        playMessage(messageObject2);
        if (z) {
            if (z2) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda41
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$startAudioAgain$7(messageObject2);
                    }
                }, 100L);
            } else {
                lambda$startAudioAgain$7(messageObject2);
            }
        }
    }

    public void setInputFieldHasText(boolean z) {
        this.inputFieldHasText = z;
    }

    public void setAllowStartRecord(boolean z) {
        this.allowStartRecord = z;
    }

    public void startRaiseToEarSensors(ChatActivity chatActivity) {
        if (chatActivity != null) {
            if ((this.accelerometerSensor == null && (this.gravitySensor == null || this.linearAcceleration == null)) || this.proximitySensor == null) {
                return;
            }
            if (!SharedConfig.enabledRaiseTo(false)) {
                MessageObject messageObject = this.playingMessageObject;
                if (messageObject == null) {
                    return;
                }
                if (!messageObject.isVoice() && !this.playingMessageObject.isRoundVideo()) {
                    return;
                }
            }
            this.raiseChat = chatActivity;
            if (this.sensorsStarted) {
                return;
            }
            float[] fArr = this.gravity;
            fArr[2] = 0.0f;
            fArr[1] = 0.0f;
            fArr[0] = 0.0f;
            float[] fArr2 = this.linearAcceleration;
            fArr2[2] = 0.0f;
            fArr2[1] = 0.0f;
            fArr2[0] = 0.0f;
            float[] fArr3 = this.gravityFast;
            fArr3[2] = 0.0f;
            fArr3[1] = 0.0f;
            fArr3[0] = 0.0f;
            this.lastTimestamp = 0L;
            this.previousAccValue = 0.0f;
            this.raisedToTop = 0;
            this.raisedToTopSign = 0;
            this.countLess = 0;
            this.raisedToBack = 0;
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startRaiseToEarSensors$8();
                }
            });
            this.sensorsStarted = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRaiseToEarSensors$8() {
        Sensor sensor = this.gravitySensor;
        if (sensor != null) {
            this.sensorManager.registerListener(this, sensor, 30000);
        }
        Sensor sensor2 = this.linearSensor;
        if (sensor2 != null) {
            this.sensorManager.registerListener(this, sensor2, 30000);
        }
        Sensor sensor3 = this.accelerometerSensor;
        if (sensor3 != null) {
            this.sensorManager.registerListener(this, sensor3, 30000);
        }
        this.sensorManager.registerListener(this, this.proximitySensor, 3);
    }

    public void stopRaiseToEarSensors(ChatActivity chatActivity, boolean z, boolean z2) {
        MediaController mediaController;
        if (this.ignoreOnPause) {
            this.ignoreOnPause = false;
            return;
        }
        if (!z2) {
            mediaController = this;
        } else if (this.recordingAudio != null && !isRecordingPaused()) {
            toggleRecordingPause(false);
            mediaController = this;
        } else {
            mediaController = this;
            mediaController.stopRecording(z ? 2 : 0, false, 0, false, 0L);
        }
        if (!mediaController.sensorsStarted || mediaController.ignoreOnPause) {
            return;
        }
        if ((mediaController.accelerometerSensor == null && (mediaController.gravitySensor == null || mediaController.linearAcceleration == null)) || mediaController.proximitySensor == null || mediaController.raiseChat != chatActivity) {
            return;
        }
        mediaController.raiseChat = null;
        mediaController.sensorsStarted = false;
        mediaController.accelerometerVertical = false;
        mediaController.proximityTouched = false;
        mediaController.raiseToEarRecord = false;
        mediaController.useFrontSpeaker = false;
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$stopRaiseToEarSensors$9();
            }
        });
        PowerManager.WakeLock wakeLock = mediaController.proximityWakeLock;
        if (wakeLock == null || !wakeLock.isHeld()) {
            return;
        }
        mediaController.proximityWakeLock.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRaiseToEarSensors$9() {
        Sensor sensor = this.linearSensor;
        if (sensor != null) {
            this.sensorManager.unregisterListener(this, sensor);
        }
        Sensor sensor2 = this.gravitySensor;
        if (sensor2 != null) {
            this.sensorManager.unregisterListener(this, sensor2);
        }
        Sensor sensor3 = this.accelerometerSensor;
        if (sensor3 != null) {
            this.sensorManager.unregisterListener(this, sensor3);
        }
        this.sensorManager.unregisterListener(this, this.proximitySensor);
    }

    public void cleanupPlayer(boolean z, boolean z2) {
        cleanupPlayer(z, z2, false, false);
    }

    public void cleanupPlayer(boolean z, boolean z2, boolean z3, boolean z4) {
        boolean z5;
        PipRoundVideoView pipRoundVideoView;
        MessageObject messageObject;
        if (z2 && restoreMusicPlaylistState()) {
            return;
        }
        if (this.audioPlayer != null) {
            ValueAnimator valueAnimator = this.audioVolumeAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllUpdateListeners();
                this.audioVolumeAnimator.cancel();
            }
            if (!CastSync.isActive() && this.audioPlayer.isPlaying() && (messageObject = this.playingMessageObject) != null && !messageObject.isVoice()) {
                final VideoPlayer videoPlayer = this.audioPlayer;
                ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.audioVolume, 0.0f);
                valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda13
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        this.f$0.lambda$cleanupPlayer$10(videoPlayer, valueAnimator2);
                    }
                });
                valueAnimatorOfFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.messenger.MediaController.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        try {
                            videoPlayer.releasePlayer(true);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                });
                valueAnimatorOfFloat.setDuration(300L);
                valueAnimatorOfFloat.start();
            } else {
                try {
                    this.audioPlayer.releasePlayer(true);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            this.audioPlayer = null;
            Theme.unrefAudioVisualizeDrawable(this.playingMessageObject);
        } else {
            VideoPlayer videoPlayer2 = this.videoPlayer;
            if (videoPlayer2 != null) {
                this.currentAspectRatioFrameLayout = null;
                this.currentTextureViewContainer = null;
                this.currentAspectRatioFrameLayoutReady = false;
                this.isDrawingWasReady = false;
                this.currentTextureView = null;
                this.goingToShowMessageObject = null;
                if (z4) {
                    PhotoViewer.getInstance().injectVideoPlayer(this.videoPlayer);
                    MessageObject messageObject2 = this.playingMessageObject;
                    this.goingToShowMessageObject = messageObject2;
                    NotificationCenter.getInstance(messageObject2.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingGoingToStop, this.playingMessageObject, Boolean.TRUE);
                } else {
                    long currentPosition = videoPlayer2.getCurrentPosition();
                    MessageObject messageObject3 = this.playingMessageObject;
                    if (messageObject3 != null && messageObject3.isVideo() && currentPosition > 0) {
                        MessageObject messageObject4 = this.playingMessageObject;
                        messageObject4.audioProgressMs = (int) currentPosition;
                        NotificationCenter.getInstance(messageObject4.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingGoingToStop, this.playingMessageObject, Boolean.FALSE);
                    }
                    this.videoPlayer.releasePlayer(true);
                    this.videoPlayer = null;
                }
                try {
                    this.baseActivity.getWindow().clearFlags(128);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (this.playingMessageObject != null && !z4) {
                    AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                    FileLoader.getInstance(this.playingMessageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
                }
            }
        }
        stopProgressTimer();
        this.lastProgress = 0L;
        this.isPaused = false;
        MessageObject messageObject5 = this.playingMessageObject;
        if (messageObject5 != null) {
            if (this.downloadingCurrentMessage) {
                FileLoader.getInstance(messageObject5.currentAccount).cancelLoadFile(this.playingMessageObject.getDocument());
            }
            MessageObject messageObject6 = this.playingMessageObject;
            if (z) {
                messageObject6.resetPlayingProgress();
                NotificationCenter.getInstance(messageObject6.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
            }
            this.playingMessageObject = null;
            this.downloadingCurrentMessage = false;
            if (z) {
                ArrayList<MessageObject> arrayList = this.voiceMessagesPlaylist;
                int iIndexOf = -1;
                if (arrayList != null) {
                    if (z3 && (iIndexOf = arrayList.indexOf(messageObject6)) >= 0) {
                        this.voiceMessagesPlaylist.remove(iIndexOf);
                        this.voiceMessagesPlaylistMap.remove(messageObject6.getId());
                        if (this.voiceMessagesPlaylist.isEmpty()) {
                            this.voiceMessagesPlaylist = null;
                            this.voiceMessagesPlaylistMap = null;
                        }
                    } else {
                        this.voiceMessagesPlaylist = null;
                        this.voiceMessagesPlaylistMap = null;
                    }
                }
                ArrayList<MessageObject> arrayList2 = this.voiceMessagesPlaylist;
                if (arrayList2 != null && iIndexOf < arrayList2.size()) {
                    MessageObject messageObject7 = this.voiceMessagesPlaylist.get(iIndexOf);
                    playMessage(messageObject7);
                    if (!messageObject7.isRoundVideo() && (pipRoundVideoView = this.pipRoundVideoView) != null) {
                        pipRoundVideoView.close(true);
                        this.pipRoundVideoView = null;
                    }
                    z5 = true;
                } else {
                    if ((messageObject6.isVoice() || messageObject6.isRoundVideo()) && messageObject6.getId() != 0) {
                        startRecordingIfFromSpeaker();
                    }
                    NotificationCenter.getInstance(messageObject6.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingDidReset, Integer.valueOf(messageObject6.getId()), Boolean.valueOf(z2));
                    this.pipSwitchingState = 0;
                    PipRoundVideoView pipRoundVideoView2 = this.pipRoundVideoView;
                    if (pipRoundVideoView2 != null) {
                        pipRoundVideoView2.close(true);
                        this.pipRoundVideoView = null;
                    }
                    z5 = false;
                }
                if (!z5) {
                    checkAudioFocus(messageObject6, false);
                }
            } else {
                z5 = false;
            }
            if (z2) {
                ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class<?>) MusicPlayerService.class));
            }
        } else {
            z5 = false;
        }
        if (!z5 && z3 && !SharedConfig.enabledRaiseTo(true)) {
            ChatActivity chatActivity = this.raiseChat;
            stopRaiseToEarSensors(chatActivity, false, false);
            this.raiseChat = chatActivity;
        }
        if (z2) {
            CastSync.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupPlayer$10(VideoPlayer videoPlayer, ValueAnimator valueAnimator) {
        videoPlayer.setVolume((this.audioFocus != 1 ? 1.0f : VOLUME_DUCK) * ((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public boolean isGoingToShowMessageObject(MessageObject messageObject) {
        return this.goingToShowMessageObject == messageObject;
    }

    public void resetGoingToShowMessageObject() {
        this.goingToShowMessageObject = null;
    }

    private boolean isSamePlayingMessage(MessageObject messageObject) {
        MessageObject messageObject2 = this.playingMessageObject;
        if (messageObject2 != null && messageObject2.getDialogId() == messageObject.getDialogId() && this.playingMessageObject.getId() == messageObject.getId()) {
            if ((this.playingMessageObject.eventId == 0) == (messageObject.eventId == 0)) {
                return true;
            }
        }
        return false;
    }

    public boolean seekToProgress(MessageObject messageObject, float f) {
        MessageObject messageObject2 = this.playingMessageObject;
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && messageObject2 != null && isSamePlayingMessage(messageObject)) {
            try {
                VideoPlayer videoPlayer = this.audioPlayer;
                if (videoPlayer != null) {
                    long duration = videoPlayer.getDuration();
                    if (duration == -9223372036854775807L) {
                        this.seekToProgressPending = f;
                    } else {
                        messageObject2.audioProgress = f;
                        long j = (int) (duration * f);
                        this.audioPlayer.seekTo(j);
                        this.lastProgress = j;
                        if (!this.ignorePlayerUpdate) {
                            CastSync.seekTo(j);
                        }
                    }
                } else {
                    VideoPlayer videoPlayer2 = this.videoPlayer;
                    if (videoPlayer2 != null) {
                        videoPlayer2.seekTo((long) (videoPlayer2.getDuration() * f));
                        if (!this.ignorePlayerUpdate) {
                            CastSync.seekTo((long) (this.videoPlayer.getDuration() * f));
                        }
                    }
                }
                NotificationCenter.getInstance(messageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingDidSeek, Integer.valueOf(messageObject2.getId()), Float.valueOf(f));
                return true;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return false;
    }

    public boolean seekToProgressMs(MessageObject messageObject, long j) {
        long duration;
        MessageObject messageObject2 = this.playingMessageObject;
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && messageObject2 != null && isSamePlayingMessage(messageObject)) {
            try {
                VideoPlayer videoPlayer = this.audioPlayer;
                if (videoPlayer != null) {
                    duration = videoPlayer.getDuration();
                    if (duration != -9223372036854775807L) {
                        messageObject2.audioProgress = Utilities.clamp01(j / duration);
                    }
                    this.audioPlayer.seekTo(j);
                    this.lastProgress = j;
                    if (!this.ignorePlayerUpdate) {
                        CastSync.seekTo(j);
                    }
                } else {
                    VideoPlayer videoPlayer2 = this.videoPlayer;
                    if (videoPlayer2 != null) {
                        duration = videoPlayer2.getDuration();
                        this.videoPlayer.seekTo(j);
                        if (!this.ignorePlayerUpdate) {
                            CastSync.seekTo(j);
                        }
                    } else {
                        duration = 1;
                    }
                }
                if (duration != 0) {
                    NotificationCenter.getInstance(messageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingDidSeek, Integer.valueOf(messageObject2.getId()), Float.valueOf(Utilities.clamp01(j / duration)));
                }
                return true;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return false;
    }

    public long getProgressMs(MessageObject messageObject) {
        MessageObject messageObject2 = this.playingMessageObject;
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && messageObject2 != null && isSamePlayingMessage(messageObject)) {
            try {
                VideoPlayer videoPlayer = this.audioPlayer;
                if (videoPlayer != null) {
                    return videoPlayer.getCurrentPosition();
                }
                VideoPlayer videoPlayer2 = this.videoPlayer;
                if (videoPlayer2 != null) {
                    return videoPlayer2.getCurrentPosition();
                }
            } catch (Exception unused) {
            }
        }
        return -1L;
    }

    public long getDuration() {
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer == null) {
            return 0L;
        }
        return videoPlayer.getDuration();
    }

    public MessageObject getPlayingMessageObject() {
        return this.playingMessageObject;
    }

    public int getPlayingMessageObjectNum() {
        return this.currentPlaylistNum;
    }

    private void buildShuffledPlayList() {
        MessageObject messageObject;
        if (this.playlist.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.playlist);
        this.shuffledPlaylist.clear();
        int i = this.currentPlaylistNum;
        if (i < 0 || i >= this.playlist.size()) {
            messageObject = null;
        } else {
            messageObject = this.playlist.get(this.currentPlaylistNum);
            arrayList.remove(this.currentPlaylistNum);
        }
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            int iNextInt = Utilities.random.nextInt(arrayList.size());
            this.shuffledPlaylist.add((MessageObject) arrayList.get(iNextInt));
            arrayList.remove(iNextInt);
        }
        if (messageObject != null) {
            this.shuffledPlaylist.add(messageObject);
            this.currentPlaylistNum = this.shuffledPlaylist.size() - 1;
        }
    }

    /* JADX WARN: Code duplicated, block: B:37:0x00e6 A[PHI: r10
  0x00e6: PHI (r10v17 long) = (r10v8 long), (r10v9 long) binds: [B:36:0x00e4, B:39:0x00ec] A[DONT_GENERATE, DONT_INLINE]] */
    public void loadMoreMusic() {
        MessageObject messageObject;
        final int i;
        long j;
        TLObject tLObject;
        MessagesController.SavedMusicList savedMusicList = this.currentSavedMusicList;
        if (savedMusicList != null) {
            savedMusicList.load();
            return;
        }
        if (this.loadingPlaylist || (messageObject = this.playingMessageObject) == null || messageObject.scheduled || DialogObject.isEncryptedDialog(messageObject.getDialogId()) || (i = this.playlistClassGuid) == 0) {
            return;
        }
        PlaylistGlobalSearchParams playlistGlobalSearchParams = this.playlistGlobalSearchParams;
        if (playlistGlobalSearchParams != null) {
            if (playlistGlobalSearchParams.endReached || this.playlist.isEmpty()) {
                return;
            }
            final int i2 = this.playlist.get(0).currentAccount;
            if (this.playlistGlobalSearchParams.dialogId != 0) {
                TLRPC.TL_messages_search tL_messages_search = new TLRPC.TL_messages_search();
                PlaylistGlobalSearchParams playlistGlobalSearchParams2 = this.playlistGlobalSearchParams;
                tL_messages_search.q = playlistGlobalSearchParams2.query;
                tL_messages_search.limit = 20;
                FiltersView.MediaFilterData mediaFilterData = playlistGlobalSearchParams2.filter;
                tL_messages_search.filter = mediaFilterData == null ? new TLRPC.TL_inputMessagesFilterEmpty() : mediaFilterData.filter;
                tL_messages_search.peer = AccountInstance.getInstance(i2).getMessagesController().getInputPeer(this.playlistGlobalSearchParams.dialogId);
                ArrayList<MessageObject> arrayList = this.playlist;
                tL_messages_search.offset_id = arrayList.get(arrayList.size() - 1).getId();
                PlaylistGlobalSearchParams playlistGlobalSearchParams3 = this.playlistGlobalSearchParams;
                long j2 = playlistGlobalSearchParams3.minDate;
                if (j2 > 0) {
                    tL_messages_search.min_date = (int) (j2 / 1000);
                }
                long j3 = playlistGlobalSearchParams3.maxDate;
                tLObject = tL_messages_search;
                if (j3 > 0) {
                    tL_messages_search.min_date = (int) (j3 / 1000);
                    tLObject = tL_messages_search;
                }
            } else {
                TLRPC.TL_messages_searchGlobal tL_messages_searchGlobal = new TLRPC.TL_messages_searchGlobal();
                tL_messages_searchGlobal.limit = 20;
                PlaylistGlobalSearchParams playlistGlobalSearchParams4 = this.playlistGlobalSearchParams;
                tL_messages_searchGlobal.q = playlistGlobalSearchParams4.query;
                tL_messages_searchGlobal.filter = playlistGlobalSearchParams4.filter.filter;
                ArrayList<MessageObject> arrayList2 = this.playlist;
                MessageObject messageObject2 = arrayList2.get(arrayList2.size() - 1);
                tL_messages_searchGlobal.offset_id = messageObject2.getId();
                PlaylistGlobalSearchParams playlistGlobalSearchParams5 = this.playlistGlobalSearchParams;
                tL_messages_searchGlobal.offset_rate = playlistGlobalSearchParams5.nextSearchRate;
                tL_messages_searchGlobal.flags |= 1;
                tL_messages_searchGlobal.folder_id = playlistGlobalSearchParams5.folderId;
                TLRPC.Peer peer = messageObject2.messageOwner.peer_id;
                long j4 = peer.channel_id;
                if (j4 != 0) {
                    j = -j4;
                } else {
                    j4 = peer.chat_id;
                    if (j4 != 0) {
                        j = -j4;
                    } else {
                        j = peer.user_id;
                    }
                }
                tL_messages_searchGlobal.offset_peer = MessagesController.getInstance(i2).getInputPeer(j);
                PlaylistGlobalSearchParams playlistGlobalSearchParams6 = this.playlistGlobalSearchParams;
                long j5 = playlistGlobalSearchParams6.minDate;
                if (j5 > 0) {
                    tL_messages_searchGlobal.min_date = (int) (j5 / 1000);
                }
                long j6 = playlistGlobalSearchParams6.maxDate;
                tLObject = tL_messages_searchGlobal;
                if (j6 > 0) {
                    tL_messages_searchGlobal.min_date = (int) (j6 / 1000);
                    tLObject = tL_messages_searchGlobal;
                }
            }
            this.loadingPlaylist = true;
            ConnectionsManager.getInstance(i2).sendRequest(tLObject, new RequestDelegate() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda21
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$loadMoreMusic$12(i, i2, tLObject2, tL_error);
                }
            });
            return;
        }
        boolean[] zArr = this.playlistEndReached;
        if (!zArr[0]) {
            this.loadingPlaylist = true;
            AccountInstance.getInstance(this.playingMessageObject.currentAccount).getMediaDataController().loadMedia(this.playingMessageObject.getDialogId(), 50, this.playlistMaxId[0], 0, 4, 0L, 1, this.playlistClassGuid, 0, null, null);
        } else {
            if (this.playlistMergeDialogId == 0 || zArr[1]) {
                return;
            }
            this.loadingPlaylist = true;
            AccountInstance.getInstance(this.playingMessageObject.currentAccount).getMediaDataController().loadMedia(this.playlistMergeDialogId, 50, this.playlistMaxId[0], 0, 4, 0L, 1, this.playlistClassGuid, 0, null, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMoreMusic$12(final int i, final int i2, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadMoreMusic$11(i, tL_error, tLObject, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMoreMusic$11(int i, TLRPC.TL_error tL_error, TLObject tLObject, int i2) {
        PlaylistGlobalSearchParams playlistGlobalSearchParams;
        if (this.playlistClassGuid != i || (playlistGlobalSearchParams = this.playlistGlobalSearchParams) == null || this.playingMessageObject == null || tL_error != null) {
            return;
        }
        this.loadingPlaylist = false;
        TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
        playlistGlobalSearchParams.nextSearchRate = messages_messages.next_rate;
        MessagesStorage.getInstance(i2).putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
        MessagesController.getInstance(i2).putUsers(messages_messages.users, false);
        MessagesController.getInstance(i2).putChats(messages_messages.chats, false);
        int size = messages_messages.messages.size();
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            MessageObject messageObject = new MessageObject(i2, (TLRPC.Message) messages_messages.messages.get(i4), false, true);
            if (!messageObject.isVoiceOnce() && !this.playlistMap.containsKey(Integer.valueOf(messageObject.getId()))) {
                this.playlist.add(0, messageObject);
                this.playlistMap.put(Integer.valueOf(messageObject.getId()), messageObject);
                i3++;
            }
        }
        sortPlaylist();
        this.loadingPlaylist = false;
        this.playlistGlobalSearchParams.endReached = this.playlist.size() == this.playlistGlobalSearchParams.totalCount;
        if (SharedConfig.shuffleMusic) {
            buildShuffledPlayList();
        }
        if (i3 != 0) {
            NotificationCenter.getInstance(this.playingMessageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.moreMusicDidLoad, Integer.valueOf(i3));
        }
    }

    public boolean setPlaylist(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j, PlaylistGlobalSearchParams playlistGlobalSearchParams) {
        return setPlaylist(arrayList, messageObject, j, true, playlistGlobalSearchParams);
    }

    public boolean setPlaylist(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j) {
        return setPlaylist(arrayList, messageObject, j, true, null);
    }

    public boolean setPlaylist(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j, boolean z, PlaylistGlobalSearchParams playlistGlobalSearchParams) {
        if (this.playingMessageObject == messageObject) {
            int iIndexOf = this.playlist.indexOf(messageObject);
            if (iIndexOf >= 0) {
                this.currentPlaylistNum = iIndexOf;
            }
            return playMessage(messageObject);
        }
        this.forceLoopCurrentPlaylist = !z;
        this.playlistMergeDialogId = j;
        this.playMusicAgain = !this.playlist.isEmpty();
        clearPlaylist();
        this.playlistGlobalSearchParams = playlistGlobalSearchParams;
        boolean z2 = false;
        if (!arrayList.isEmpty() && DialogObject.isEncryptedDialog(arrayList.get(0).getDialogId())) {
            z2 = true;
        }
        int iMin = Integer.MAX_VALUE;
        int iMax = Integer.MIN_VALUE;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            MessageObject messageObject2 = arrayList.get(size);
            if (messageObject2.isMusic()) {
                int id = messageObject2.getId();
                if (id > 0 || z2) {
                    iMin = Math.min(iMin, id);
                    iMax = Math.max(iMax, id);
                }
                this.playlist.add(messageObject2);
                this.playlistMap.put(Integer.valueOf(id), messageObject2);
            }
        }
        sortPlaylist();
        int iIndexOf2 = this.playlist.indexOf(messageObject);
        this.currentPlaylistNum = iIndexOf2;
        if (iIndexOf2 == -1) {
            clearPlaylist();
            this.currentPlaylistNum = this.playlist.size();
            this.playlist.add(messageObject);
            this.playlistMap.put(Integer.valueOf(messageObject.getId()), messageObject);
        }
        if (messageObject.isMusic() && !messageObject.scheduled) {
            if (SharedConfig.shuffleMusic) {
                buildShuffledPlayList();
            }
            if (z) {
                if (this.playlistGlobalSearchParams == null) {
                    MediaDataController.getInstance(messageObject.currentAccount).loadMusic(messageObject.getDialogId(), iMin, iMax);
                } else {
                    this.playlistClassGuid = ConnectionsManager.generateClassGuid();
                }
            }
        }
        return playMessage(messageObject);
    }

    private void sortPlaylist() {
        Collections.sort(this.playlist, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda40
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return MediaController.$r8$lambda$Z0Erp2L8slKvvC3qPEU0fm69vKk((MessageObject) obj, (MessageObject) obj2);
            }
        });
    }

    public static /* synthetic */ int $r8$lambda$Z0Erp2L8slKvvC3qPEU0fm69vKk(MessageObject messageObject, MessageObject messageObject2) {
        int iCompare;
        int id = messageObject.getId();
        int id2 = messageObject2.getId();
        long j = messageObject.messageOwner.grouped_id;
        long j2 = messageObject2.messageOwner.grouped_id;
        if (id >= 0 || id2 >= 0) {
            if (j != 0 && j == j2) {
                iCompare = Integer.compare(id2, id);
            } else {
                return Integer.compare(id, id2);
            }
        } else if (j != 0 && j == j2) {
            iCompare = Integer.compare(id, id2);
        } else {
            return Integer.compare(id2, id);
        }
        return -iCompare;
    }

    public boolean hasNoNextVoiceOrRoundVideoMessage() {
        ArrayList<MessageObject> arrayList;
        MessageObject messageObject = this.playingMessageObject;
        return messageObject == null || !(messageObject.isVoice() || this.playingMessageObject.isRoundVideo()) || (arrayList = this.voiceMessagesPlaylist) == null || arrayList.size() <= 1 || !this.voiceMessagesPlaylist.contains(this.playingMessageObject) || this.voiceMessagesPlaylist.indexOf(this.playingMessageObject) >= this.voiceMessagesPlaylist.size() - 1;
    }

    public void playNextMessage() {
        playNextMessageWithoutOrder(false);
    }

    public boolean findMessageInPlaylistAndPlay(MessageObject messageObject) {
        int iIndexOf = this.playlist.indexOf(messageObject);
        if (iIndexOf == -1) {
            return playMessage(messageObject);
        }
        playMessageAtIndex(iIndexOf);
        return true;
    }

    public void playMessageAtIndex(int i) {
        int i2 = this.currentPlaylistNum;
        if (i2 < 0 || i2 >= this.playlist.size()) {
            return;
        }
        this.currentPlaylistNum = i;
        this.playMusicAgain = true;
        MessageObject messageObject = this.playlist.get(i);
        if (this.playingMessageObject != null && !isSamePlayingMessage(messageObject)) {
            this.playingMessageObject.resetPlayingProgress();
        }
        playMessage(messageObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playNextMessageWithoutOrder(boolean z) {
        int i;
        ArrayList<MessageObject> arrayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
        if (z && (((i = SharedConfig.repeatMode) == 2 || (i == 1 && arrayList.size() == 1)) && !this.forceLoopCurrentPlaylist)) {
            cleanupPlayer(false, false);
            int i2 = this.currentPlaylistNum;
            if (i2 < 0 || i2 >= arrayList.size()) {
                return;
            }
            MessageObject messageObject = arrayList.get(this.currentPlaylistNum);
            messageObject.audioProgress = 0.0f;
            messageObject.audioProgressSec = 0;
            playMessage(messageObject);
            return;
        }
        if (traversePlaylist(arrayList, SharedConfig.playOrderReversed ? 1 : -1) && z && SharedConfig.repeatMode == 0 && !this.forceLoopCurrentPlaylist) {
            VideoPlayer videoPlayer = this.audioPlayer;
            if (videoPlayer == null && this.videoPlayer == null) {
                return;
            }
            if (videoPlayer != null) {
                try {
                    videoPlayer.releasePlayer(true);
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.audioPlayer = null;
                Theme.unrefAudioVisualizeDrawable(this.playingMessageObject);
            } else {
                this.currentAspectRatioFrameLayout = null;
                this.currentTextureViewContainer = null;
                this.currentAspectRatioFrameLayoutReady = false;
                this.currentTextureView = null;
                this.videoPlayer.releasePlayer(true);
                this.videoPlayer = null;
                try {
                    this.baseActivity.getWindow().clearFlags(128);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                FileLoader.getInstance(this.playingMessageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
            }
            stopProgressTimer();
            this.lastProgress = 0L;
            this.isPaused = true;
            MessageObject messageObject2 = this.playingMessageObject;
            messageObject2.audioProgress = 0.0f;
            messageObject2.audioProgressSec = 0;
            NotificationCenter.getInstance(messageObject2.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
            NotificationCenter.getInstance(this.playingMessageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
            return;
        }
        int i3 = this.currentPlaylistNum;
        if (i3 < 0 || i3 >= arrayList.size()) {
            return;
        }
        MessageObject messageObject3 = this.playingMessageObject;
        if (messageObject3 != null) {
            messageObject3.resetPlayingProgress();
        }
        this.playMusicAgain = true;
        playMessage(arrayList.get(this.currentPlaylistNum));
    }

    public void playPreviousMessage() {
        int i;
        ArrayList<MessageObject> arrayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
        if (arrayList.isEmpty() || (i = this.currentPlaylistNum) < 0 || i >= arrayList.size()) {
            return;
        }
        MessageObject messageObject = arrayList.get(this.currentPlaylistNum);
        if (messageObject.audioProgressSec > 10) {
            seekToProgress(messageObject, 0.0f);
            return;
        }
        traversePlaylist(arrayList, SharedConfig.playOrderReversed ? -1 : 1);
        if (this.currentPlaylistNum >= arrayList.size()) {
            return;
        }
        this.playMusicAgain = true;
        playMessage(arrayList.get(this.currentPlaylistNum));
    }

    /* JADX WARN: Code duplicated, block: B:39:0x006e  */
    private boolean traversePlaylist(ArrayList<MessageObject> arrayList, int i) {
        MessageObject messageObject;
        int i2;
        MessageObject messageObject2;
        int i3 = this.currentPlaylistNum;
        boolean z = ConnectionsManager.getInstance(UserConfig.selectedAccount).getConnectionState() == 2;
        this.currentPlaylistNum += i;
        if (z) {
            while (this.currentPlaylistNum < arrayList.size() && (i2 = this.currentPlaylistNum) >= 0 && ((messageObject2 = arrayList.get(i2)) == null || !messageObject2.mediaExists)) {
                this.currentPlaylistNum += i;
            }
        }
        if (this.currentPlaylistNum < arrayList.size() && this.currentPlaylistNum >= 0) {
            return false;
        }
        this.currentPlaylistNum = this.currentPlaylistNum >= arrayList.size() ? 0 : arrayList.size() - 1;
        if (z) {
            while (true) {
                int i4 = this.currentPlaylistNum;
                if (i4 >= 0 && i4 < arrayList.size()) {
                    int i5 = this.currentPlaylistNum;
                    if (i <= 0) {
                        if (i5 < i3) {
                            break;
                        }
                        messageObject = arrayList.get(this.currentPlaylistNum);
                        if (messageObject == null) {
                        }
                        this.currentPlaylistNum += i;
                    } else {
                        if (i5 > i3) {
                            break;
                        }
                        messageObject = arrayList.get(this.currentPlaylistNum);
                        if (messageObject == null && messageObject.mediaExists) {
                            break;
                        }
                        this.currentPlaylistNum += i;
                    }
                } else {
                    break;
                }
            }
            if (this.currentPlaylistNum >= arrayList.size() || this.currentPlaylistNum < 0) {
                this.currentPlaylistNum = this.currentPlaylistNum < arrayList.size() ? arrayList.size() - 1 : 0;
            }
        }
        return true;
    }

    protected void checkIsNextMediaFileDownloaded() {
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject == null || !messageObject.isMusic()) {
            return;
        }
        checkIsNextMusicFileDownloaded(this.playingMessageObject.currentAccount);
    }

    private void checkIsNextVoiceFileDownloaded(int i) {
        ArrayList<MessageObject> arrayList = this.voiceMessagesPlaylist;
        if (arrayList != null) {
            if (arrayList.size() < 2) {
                return;
            }
            MessageObject messageObject = this.voiceMessagesPlaylist.get(1);
            String str = messageObject.messageOwner.attachPath;
            File file = null;
            if (str != null && str.length() > 0) {
                File file2 = new File(messageObject.messageOwner.attachPath);
                if (file2.exists()) {
                    file = file2;
                }
            }
            File pathToMessage = file != null ? file : FileLoader.getInstance(i).getPathToMessage(messageObject.messageOwner);
            pathToMessage.exists();
            if (pathToMessage == file || pathToMessage.exists()) {
                return;
            }
            FileLoader.getInstance(i).loadFile(messageObject.getDocument(), messageObject, 0, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 0);
        }
    }

    private void checkIsNextMusicFileDownloaded(int i) {
        int size;
        if (DownloadController.getInstance(i).canDownloadNextTrack()) {
            ArrayList<MessageObject> arrayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
            if (arrayList != null) {
                if (arrayList.size() < 2) {
                    return;
                }
                if (SharedConfig.playOrderReversed) {
                    size = this.currentPlaylistNum + 1;
                    if (size >= arrayList.size()) {
                        size = 0;
                    }
                } else {
                    size = this.currentPlaylistNum - 1;
                    if (size < 0) {
                        size = arrayList.size() - 1;
                    }
                }
                if (size < 0 || size >= arrayList.size()) {
                    return;
                }
                MessageObject messageObject = arrayList.get(size);
                File file = null;
                if (!TextUtils.isEmpty(messageObject.messageOwner.attachPath)) {
                    File file2 = new File(messageObject.messageOwner.attachPath);
                    if (file2.exists()) {
                        file = file2;
                    }
                }
                File pathToMessage = file != null ? file : FileLoader.getInstance(i).getPathToMessage(messageObject.messageOwner);
                pathToMessage.exists();
                if (pathToMessage == file || pathToMessage.exists() || !messageObject.isMusic()) {
                    return;
                }
                FileLoader.getInstance(i).loadFile(messageObject.getDocument(), messageObject, 0, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 0);
            }
        }
    }

    public void setVoiceMessagesPlaylist(ArrayList<MessageObject> arrayList, boolean z) {
        ArrayList<MessageObject> arrayList2 = arrayList != null ? new ArrayList<>(arrayList) : null;
        this.voiceMessagesPlaylist = arrayList2;
        if (arrayList2 != null) {
            this.voiceMessagesPlaylistUnread = z;
            this.voiceMessagesPlaylistMap = new SparseArray<>();
            for (int i = 0; i < this.voiceMessagesPlaylist.size(); i++) {
                MessageObject messageObject = this.voiceMessagesPlaylist.get(i);
                this.voiceMessagesPlaylistMap.put(messageObject.getId(), messageObject);
            }
        }
    }

    private void checkAudioFocus(MessageObject messageObject, boolean z) {
        int i;
        int iRequestAudioFocus;
        if (messageObject.isVoice() || messageObject.isRoundVideo()) {
            i = this.useFrontSpeaker ? 3 : 2;
        } else {
            i = 1;
        }
        int i2 = this.hasAudioFocus;
        if (i2 != i && z) {
            this.hasAudioFocus = i;
            if (i == 3) {
                iRequestAudioFocus = NotificationsController.audioManager.requestAudioFocus(this, 0, 1);
            } else {
                iRequestAudioFocus = NotificationsController.audioManager.requestAudioFocus(this, 3, (i != 2 || SharedConfig.pauseMusicOnMedia) ? 2 : 3);
            }
            if (iRequestAudioFocus == 1) {
                this.audioFocus = 2;
                return;
            }
            return;
        }
        if (i2 == 0 || z || NotificationsController.audioManager.abandonAudioFocus(this) != 1) {
            return;
        }
        this.audioFocus = 0;
        this.hasAudioFocus = 0;
    }

    public boolean isPiPShown() {
        return this.pipRoundVideoView != null;
    }

    public void setCurrentVideoVisible(boolean z) {
        AspectRatioFrameLayout aspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        if (aspectRatioFrameLayout == null) {
            return;
        }
        if (z) {
            PipRoundVideoView pipRoundVideoView = this.pipRoundVideoView;
            if (pipRoundVideoView != null) {
                this.pipSwitchingState = 2;
                pipRoundVideoView.close(true);
                this.pipRoundVideoView = null;
                return;
            } else {
                if (aspectRatioFrameLayout.getParent() == null) {
                    this.currentTextureViewContainer.addView(this.currentAspectRatioFrameLayout);
                }
                this.videoPlayer.setTextureView(this.currentTextureView);
                return;
            }
        }
        if (aspectRatioFrameLayout.getParent() != null) {
            this.pipSwitchingState = 1;
            this.currentTextureViewContainer.removeView(this.currentAspectRatioFrameLayout);
            return;
        }
        if (this.pipRoundVideoView == null) {
            try {
                PipRoundVideoView pipRoundVideoView2 = new PipRoundVideoView();
                this.pipRoundVideoView = pipRoundVideoView2;
                pipRoundVideoView2.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setCurrentVideoVisible$14();
                    }
                });
            } catch (Exception unused) {
                this.pipRoundVideoView = null;
            }
        }
        PipRoundVideoView pipRoundVideoView3 = this.pipRoundVideoView;
        if (pipRoundVideoView3 != null) {
            this.videoPlayer.setTextureView(pipRoundVideoView3.getTextureView());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCurrentVideoVisible$14() {
        cleanupPlayer(true, true);
    }

    public void setTextureView(TextureView textureView, AspectRatioFrameLayout aspectRatioFrameLayout, FrameLayout frameLayout, boolean z) {
        setTextureView(textureView, aspectRatioFrameLayout, frameLayout, z, null);
    }

    public void setTextureView(TextureView textureView, AspectRatioFrameLayout aspectRatioFrameLayout, FrameLayout frameLayout, boolean z, Runnable runnable) {
        if (textureView == null) {
            return;
        }
        if (!z && this.currentTextureView == textureView) {
            this.pipSwitchingState = 1;
            this.currentTextureView = null;
            this.currentAspectRatioFrameLayout = null;
            this.currentTextureViewContainer = null;
            return;
        }
        if (this.videoPlayer == null || textureView == this.currentTextureView) {
            return;
        }
        this.isDrawingWasReady = aspectRatioFrameLayout != null && aspectRatioFrameLayout.isDrawingReady();
        this.currentTextureView = textureView;
        if (runnable != null && this.pipRoundVideoView == null) {
            try {
                PipRoundVideoView pipRoundVideoView = new PipRoundVideoView();
                this.pipRoundVideoView = pipRoundVideoView;
                pipRoundVideoView.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda49
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setTextureView$15();
                    }
                });
            } catch (Exception unused) {
                this.pipRoundVideoView = null;
            }
        }
        PipRoundVideoView pipRoundVideoView2 = this.pipRoundVideoView;
        if (pipRoundVideoView2 != null) {
            this.videoPlayer.setTextureView(pipRoundVideoView2.getTextureView());
        } else {
            this.videoPlayer.setTextureView(this.currentTextureView);
        }
        this.currentAspectRatioFrameLayout = aspectRatioFrameLayout;
        this.currentTextureViewContainer = frameLayout;
        if (!this.currentAspectRatioFrameLayoutReady || aspectRatioFrameLayout == null) {
            return;
        }
        aspectRatioFrameLayout.setAspectRatio(this.currentAspectRatioFrameLayoutRatio, this.currentAspectRatioFrameLayoutRotation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setTextureView$15() {
        cleanupPlayer(true, true);
    }

    public void setBaseActivity(Activity activity, boolean z) {
        if (z) {
            this.baseActivity = activity;
        } else if (this.baseActivity == activity) {
            this.baseActivity = null;
        }
    }

    public void setFeedbackView(View view, boolean z) {
        if (z) {
            this.feedbackView = view;
        } else if (this.feedbackView == view) {
            this.feedbackView = null;
        }
    }

    public void setPlaybackSpeed(boolean z, float f) {
        if (z) {
            if (this.currentMusicPlaybackSpeed >= 6.0f && f == 1.0f && this.playingMessageObject != null) {
                this.audioPlayer.pause();
                final MessageObject messageObject = this.playingMessageObject;
                final float f2 = messageObject.audioProgress;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setPlaybackSpeed$16(messageObject, f2);
                    }
                }, 50L);
            }
            this.currentMusicPlaybackSpeed = f;
            if (Math.abs(f - 1.0f) > 0.001f) {
                this.fastMusicPlaybackSpeed = f;
            }
        } else {
            this.currentPlaybackSpeed = f;
            if (Math.abs(f - 1.0f) > 0.001f) {
                this.fastPlaybackSpeed = f;
            }
        }
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer != null) {
            videoPlayer.setPlaybackSpeed(Math.round(f * 10.0f) / 10.0f);
        } else {
            VideoPlayer videoPlayer2 = this.videoPlayer;
            if (videoPlayer2 != null) {
                videoPlayer2.setPlaybackSpeed(Math.round(f * 10.0f) / 10.0f);
            }
        }
        MessagesController.getGlobalMainSettings().edit().putFloat(z ? "musicPlaybackSpeed" : "playbackSpeed", f).putFloat(z ? "fastMusicPlaybackSpeed" : "fastPlaybackSpeed", z ? this.fastMusicPlaybackSpeed : this.fastPlaybackSpeed).apply();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingSpeedChanged, new Object[0]);
        if (this.ignorePlayerUpdate) {
            return;
        }
        CastSync.setSpeed(f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setPlaybackSpeed$16(MessageObject messageObject, float f) {
        if (this.audioPlayer == null || this.playingMessageObject == null || this.isPaused) {
            return;
        }
        if (isSamePlayingMessage(messageObject)) {
            seekToProgress(this.playingMessageObject, f);
        }
        this.audioPlayer.play();
    }

    public float getPlaybackSpeed(boolean z) {
        return z ? this.currentMusicPlaybackSpeed : this.currentPlaybackSpeed;
    }

    public float getFastPlaybackSpeed(boolean z) {
        return z ? this.fastMusicPlaybackSpeed : this.fastPlaybackSpeed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVideoState(MessageObject messageObject, int[] iArr, boolean z, boolean z2, int i) {
        MessageObject messageObject2;
        if (this.videoPlayer == null) {
            return;
        }
        if (i != 4 && i != 1) {
            try {
                this.baseActivity.getWindow().addFlags(128);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            try {
                this.baseActivity.getWindow().clearFlags(128);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        if (i == 3) {
            this.playerWasReady = true;
            MessageObject messageObject3 = this.playingMessageObject;
            if (messageObject3 != null && (messageObject3.isVideo() || this.playingMessageObject.isRoundVideo())) {
                AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                FileLoader.getInstance(messageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
            }
            this.currentAspectRatioFrameLayoutReady = true;
            return;
        }
        if (i == 2) {
            if (!z2 || (messageObject2 = this.playingMessageObject) == null) {
                return;
            }
            if (messageObject2.isVideo() || this.playingMessageObject.isRoundVideo()) {
                if (this.playerWasReady) {
                    this.setLoadingRunnable.run();
                    return;
                } else {
                    AndroidUtilities.runOnUIThread(this.setLoadingRunnable, 1000L);
                    return;
                }
            }
            return;
        }
        if (this.videoPlayer.isPlaying() && i == 4) {
            MessageObject messageObject4 = this.playingMessageObject;
            if (messageObject4 != null && messageObject4.isVideo() && !z && (iArr == null || iArr[0] < 4)) {
                this.videoPlayer.seekTo(0L);
                if (iArr != null) {
                    iArr[0] = iArr[0] + 1;
                    return;
                }
                return;
            }
            if (restoreMusicPlaylistState()) {
                return;
            }
            cleanupPlayer(true, hasNoNextVoiceOrRoundVideoMessage(), true, false);
        }
    }

    public void injectVideoPlayer(VideoPlayer videoPlayer, MessageObject messageObject) {
        if (videoPlayer == null || messageObject == null) {
            return;
        }
        FileLoader.getInstance(messageObject.currentAccount).setLoadingVideoForPlayer(messageObject.getDocument(), true);
        this.playerWasReady = false;
        clearPlaylist();
        this.videoPlayer = videoPlayer;
        this.playingMessageObject = messageObject;
        int i = this.playerNum + 1;
        this.playerNum = i;
        videoPlayer.setDelegate(new AnonymousClass7(i, messageObject, null, true));
        this.currentAspectRatioFrameLayoutReady = false;
        TextureView textureView = this.currentTextureView;
        if (textureView != null) {
            this.videoPlayer.setTextureView(textureView);
        }
        checkAudioFocus(messageObject, true);
        setPlayerVolume();
        this.isPaused = false;
        this.lastProgress = 0L;
        MessageObject messageObject2 = this.playingMessageObject;
        this.playingMessageObject = messageObject;
        if (!SharedConfig.enabledRaiseTo(true)) {
            startRaiseToEarSensors(this.raiseChat);
        }
        startProgressTimer(this.playingMessageObject);
        NotificationCenter.getInstance(messageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingDidStart, messageObject, messageObject2);
    }

    /* JADX INFO: renamed from: org.telegram.messenger.MediaController$7, reason: invalid class name */
    class AnonymousClass7 implements VideoPlayer.VideoPlayerDelegate {
        final /* synthetic */ boolean val$destroyAtEnd;
        final /* synthetic */ MessageObject val$messageObject;
        final /* synthetic */ int[] val$playCount;
        final /* synthetic */ int val$tag;

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onRenderedFirstFrame(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekFinished(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekStarted(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        AnonymousClass7(int i, MessageObject messageObject, int[] iArr, boolean z) {
            this.val$tag = i;
            this.val$messageObject = messageObject;
            this.val$playCount = iArr;
            this.val$destroyAtEnd = z;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onStateChanged(boolean z, int i) {
            if (this.val$tag != MediaController.this.playerNum) {
                return;
            }
            MediaController.this.updateVideoState(this.val$messageObject, this.val$playCount, this.val$destroyAtEnd, z, i);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
            FileLog.e(exc);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onVideoSizeChanged(int i, int i2, int i3, float f) {
            MediaController.this.currentAspectRatioFrameLayoutRotation = i3;
            if (i3 != 90 && i3 != 270) {
                i2 = i;
                i = i2;
            }
            MediaController.this.currentAspectRatioFrameLayoutRatio = i == 0 ? 1.0f : (i2 * f) / i;
            if (MediaController.this.currentAspectRatioFrameLayout != null) {
                MediaController.this.currentAspectRatioFrameLayout.setAspectRatio(MediaController.this.currentAspectRatioFrameLayoutRatio, MediaController.this.currentAspectRatioFrameLayoutRotation);
            }
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
            if (MediaController.this.currentAspectRatioFrameLayout == null || MediaController.this.currentAspectRatioFrameLayout.isDrawingReady()) {
                return;
            }
            MediaController.this.isDrawingWasReady = true;
            MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
            MediaController.this.currentTextureViewContainer.setTag(1);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            if (MediaController.this.videoPlayer == null) {
                return false;
            }
            if (MediaController.this.pipSwitchingState == 2) {
                if (MediaController.this.currentAspectRatioFrameLayout != null) {
                    if (MediaController.this.isDrawingWasReady) {
                        MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                    }
                    if (MediaController.this.currentAspectRatioFrameLayout.getParent() == null) {
                        MediaController.this.currentTextureViewContainer.addView(MediaController.this.currentAspectRatioFrameLayout);
                    }
                    if (MediaController.this.currentTextureView.getSurfaceTexture() != surfaceTexture) {
                        MediaController.this.currentTextureView.setSurfaceTexture(surfaceTexture);
                    }
                    MediaController.this.videoPlayer.setTextureView(MediaController.this.currentTextureView);
                }
                MediaController.this.pipSwitchingState = 0;
                return true;
            }
            if (MediaController.this.pipSwitchingState == 1) {
                if (MediaController.this.baseActivity != null) {
                    if (MediaController.this.pipRoundVideoView == null) {
                        try {
                            MediaController.this.pipRoundVideoView = new PipRoundVideoView();
                            MediaController.this.pipRoundVideoView.show(MediaController.this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$7$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$onSurfaceDestroyed$0();
                                }
                            });
                        } catch (Exception unused) {
                            MediaController.this.pipRoundVideoView = null;
                        }
                    }
                    if (MediaController.this.pipRoundVideoView != null) {
                        if (MediaController.this.pipRoundVideoView.getTextureView().getSurfaceTexture() != surfaceTexture) {
                            MediaController.this.pipRoundVideoView.getTextureView().setSurfaceTexture(surfaceTexture);
                        }
                        MediaController.this.videoPlayer.setTextureView(MediaController.this.pipRoundVideoView.getTextureView());
                    }
                }
                MediaController.this.pipSwitchingState = 0;
                return true;
            }
            if (!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isInjectingVideoPlayer()) {
                return false;
            }
            PhotoViewer.getInstance().injectVideoPlayerSurface(surfaceTexture);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceDestroyed$0() {
            MediaController.this.cleanupPlayer(true, true);
        }
    }

    public void playEmojiSound(final AccountInstance accountInstance, String str, final MessagesController.EmojiSound emojiSound, final boolean z) {
        if (emojiSound == null) {
            return;
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$playEmojiSound$19(emojiSound, accountInstance, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playEmojiSound$19(MessagesController.EmojiSound emojiSound, final AccountInstance accountInstance, boolean z) {
        final TLRPC.TL_document tL_document = new TLRPC.TL_document();
        tL_document.access_hash = emojiSound.accessHash;
        tL_document.id = emojiSound.id;
        tL_document.mime_type = "sound/ogg";
        tL_document.file_reference = emojiSound.fileReference;
        tL_document.dc_id = accountInstance.getConnectionsManager().getCurrentDatacenterId();
        final File pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document, true);
        if (!pathToAttach.exists()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    accountInstance.getFileLoader().loadFile(tL_document, null, 1, 1);
                }
            });
        } else {
            if (z) {
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$playEmojiSound$17(pathToAttach);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playEmojiSound$17(File file) {
        try {
            int i = this.emojiSoundPlayerNum + 1;
            this.emojiSoundPlayerNum = i;
            VideoPlayer videoPlayer = this.emojiSoundPlayer;
            if (videoPlayer != null) {
                videoPlayer.releasePlayer(true);
            }
            VideoPlayer videoPlayer2 = new VideoPlayer(false, false);
            this.emojiSoundPlayer = videoPlayer2;
            videoPlayer2.setDelegate(new AnonymousClass8(i));
            this.emojiSoundPlayer.preparePlayer(Uri.fromFile(file), "other");
            this.emojiSoundPlayer.setStreamType(3);
            this.emojiSoundPlayer.play();
        } catch (Exception e) {
            FileLog.e(e);
            VideoPlayer videoPlayer3 = this.emojiSoundPlayer;
            if (videoPlayer3 != null) {
                videoPlayer3.releasePlayer(true);
                this.emojiSoundPlayer = null;
            }
        }
    }

    /* JADX INFO: renamed from: org.telegram.messenger.MediaController$8, reason: invalid class name */
    class AnonymousClass8 implements VideoPlayer.VideoPlayerDelegate {
        final /* synthetic */ int val$tag;

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onRenderedFirstFrame(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekFinished(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekStarted(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onVideoSizeChanged(int i, int i2, int i3, float f) {
        }

        AnonymousClass8(int i) {
            this.val$tag = i;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onStateChanged(boolean z, final int i) {
            final int i2 = this.val$tag;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$8$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStateChanged$0(i2, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStateChanged$0(int i, int i2) {
            if (i == MediaController.this.emojiSoundPlayerNum && i2 == 4 && MediaController.this.emojiSoundPlayer != null) {
                try {
                    MediaController.this.emojiSoundPlayer.releasePlayer(true);
                    MediaController.this.emojiSoundPlayer = null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public void checkVolumeBarUI() {
        if (this.isSilent) {
            return;
        }
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (Math.abs(jCurrentTimeMillis - volumeBarLastTimeShown) < 5000) {
                return;
            }
            AudioManager audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            int i = this.useFrontSpeaker ? 0 : 3;
            int streamVolume = audioManager.getStreamVolume(i);
            if (streamVolume == 0) {
                audioManager.adjustStreamVolume(i, streamVolume, 1);
                volumeBarLastTimeShown = jCurrentTimeMillis;
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Code duplicated, block: B:36:? A[RETURN, SYNTHETIC] */
    private void setBluetoothScoOn(boolean z) {
        AudioManager audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        if (SharedConfig.recordViaSco && !PermissionRequest.hasPermission("android.permission.BLUETOOTH_CONNECT")) {
            SharedConfig.recordViaSco = false;
            SharedConfig.saveConfig();
        }
        if (!(audioManager.isBluetoothScoAvailableOffCall() && SharedConfig.recordViaSco) && z) {
            return;
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            try {
                if (defaultAdapter.getProfileConnectionState(1) != 2) {
                    if (z) {
                        return;
                    }
                }
            } catch (SecurityException unused) {
                return;
            } catch (Throwable th) {
                FileLog.e(th);
                return;
            }
        } else if (z) {
            return;
        }
        if (z && !audioManager.isBluetoothScoOn()) {
            audioManager.startBluetoothSco();
        } else {
            if (z || !audioManager.isBluetoothScoOn()) {
                return;
            }
            audioManager.stopBluetoothSco();
        }
    }

    public boolean playMessage(MessageObject messageObject) {
        return playMessage(messageObject, false);
    }

    /* JADX WARN: Code duplicated, block: B:183:0x0453  */
    /* JADX WARN: Code duplicated, block: B:185:0x045f  */
    /* JADX WARN: Code duplicated, block: B:189:0x046b  */
    /* JADX WARN: Code duplicated, block: B:192:0x0470  */
    /* JADX WARN: Code duplicated, block: B:195:0x0483  */
    /* JADX WARN: Code duplicated, block: B:197:0x0486  */
    /* JADX WARN: Code duplicated, block: B:200:0x04c5  */
    /* JADX WARN: Code duplicated, block: B:206:0x04e2  */
    /* JADX WARN: Code duplicated, block: B:213:0x04ff  */
    /* JADX WARN: Code duplicated, block: B:215:0x050a  */
    /* JADX WARN: Code duplicated, block: B:217:0x0510 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:220:0x051e  */
    /* JADX WARN: Code duplicated, block: B:228:0x05a9  */
    /* JADX WARN: Code duplicated, block: B:229:0x05aa A[Catch: Exception -> 0x05da, TryCatch #3 {Exception -> 0x05da, blocks: (B:226:0x0541, B:230:0x05ad, B:229:0x05aa), top: B:320:0x0541 }] */
    /* JADX WARN: Code duplicated, block: B:236:0x05e4  */
    /* JADX WARN: Code duplicated, block: B:238:0x05ea  */
    /* JADX WARN: Code duplicated, block: B:239:0x05ec  */
    /* JADX WARN: Code duplicated, block: B:242:0x05fe  */
    /* JADX WARN: Code duplicated, block: B:245:0x0614  */
    /* JADX WARN: Code duplicated, block: B:246:0x061e  */
    /* JADX WARN: Code duplicated, block: B:249:0x063b  */
    /* JADX WARN: Code duplicated, block: B:261:0x0661  */
    /* JADX WARN: Code duplicated, block: B:267:0x0693 A[Catch: Exception -> 0x06a5, TryCatch #7 {Exception -> 0x06a5, blocks: (B:265:0x068b, B:267:0x0693, B:269:0x069b, B:272:0x06a7, B:274:0x06b2, B:275:0x06b6), top: B:327:0x068b }] */
    /* JADX WARN: Code duplicated, block: B:269:0x069b A[Catch: Exception -> 0x06a5, TryCatch #7 {Exception -> 0x06a5, blocks: (B:265:0x068b, B:267:0x0693, B:269:0x069b, B:272:0x06a7, B:274:0x06b2, B:275:0x06b6), top: B:327:0x068b }] */
    /* JADX WARN: Code duplicated, block: B:274:0x06b2 A[Catch: Exception -> 0x06a5, TryCatch #7 {Exception -> 0x06a5, blocks: (B:265:0x068b, B:267:0x0693, B:269:0x069b, B:272:0x06a7, B:274:0x06b2, B:275:0x06b6), top: B:327:0x068b }] */
    /* JADX WARN: Code duplicated, block: B:279:0x06ed  */
    /* JADX WARN: Code duplicated, block: B:283:0x06fb A[Catch: Exception -> 0x070d, TryCatch #2 {Exception -> 0x070d, blocks: (B:281:0x06f1, B:283:0x06fb, B:285:0x0703, B:288:0x070f, B:290:0x0720), top: B:318:0x06f1 }] */
    /* JADX WARN: Code duplicated, block: B:285:0x0703 A[Catch: Exception -> 0x070d, TryCatch #2 {Exception -> 0x070d, blocks: (B:281:0x06f1, B:283:0x06fb, B:285:0x0703, B:288:0x070f, B:290:0x0720), top: B:318:0x06f1 }] */
    /* JADX WARN: Code duplicated, block: B:290:0x0720 A[Catch: Exception -> 0x070d, TRY_LEAVE, TryCatch #2 {Exception -> 0x070d, blocks: (B:281:0x06f1, B:283:0x06fb, B:285:0x0703, B:288:0x070f, B:290:0x0720), top: B:318:0x06f1 }] */
    /* JADX WARN: Code duplicated, block: B:295:0x0752  */
    /* JADX WARN: Code duplicated, block: B:301:0x0768  */
    /* JADX WARN: Code duplicated, block: B:304:0x077e A[Catch: Exception -> 0x0796, TryCatch #6 {Exception -> 0x0796, blocks: (B:302:0x0777, B:304:0x077e, B:306:0x0788, B:310:0x0798), top: B:325:0x0777 }] */
    /* JADX WARN: Code duplicated, block: B:306:0x0788 A[Catch: Exception -> 0x0796, TryCatch #6 {Exception -> 0x0796, blocks: (B:302:0x0777, B:304:0x077e, B:306:0x0788, B:310:0x0798), top: B:325:0x0777 }] */
    /* JADX WARN: Code duplicated, block: B:318:0x06f1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:320:0x0541 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:323:0x04e6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:327:0x068b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:331:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:51:0x00cd  */
    /* JADX WARN: Code duplicated, block: B:52:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:68:0x010e  */
    /* JADX WARN: Code duplicated, block: B:90:0x0185  */
    /* JADX WARN: Code duplicated, block: B:91:0x018b  */
    /* JADX WARN: Code duplicated, block: B:94:0x0194  */
    /* JADX WARN: Code duplicated, block: B:98:0x01d4  */
    public boolean playMessage(final MessageObject messageObject, boolean z) {
        boolean z2;
        boolean zExists;
        File file;
        final File pathToMessage;
        boolean z3;
        boolean z4;
        AspectRatioFrameLayout aspectRatioFrameLayout;
        boolean zIsVideo;
        boolean z5;
        final File file2;
        String str;
        String str2;
        int[] iArr;
        String str3;
        String str4;
        PipRoundVideoView pipRoundVideoView;
        byte[] bArr;
        int i;
        float f;
        TLRPC.Message message;
        char c;
        VideoPlayer videoPlayer;
        long duration;
        MessageObject messageObject2;
        int i2;
        int i3;
        VideoPlayer videoPlayer2;
        long duration2;
        long j;
        PowerManager.WakeLock wakeLock;
        TLRPC.Message message2;
        if (messageObject == null) {
            return false;
        }
        this.isSilent = z;
        checkVolumeBarUI();
        if ((this.audioPlayer != null || this.videoPlayer != null) && isSamePlayingMessage(messageObject)) {
            if (this.isPaused) {
                resumeAudio(messageObject);
            }
            if (!SharedConfig.enabledRaiseTo(true)) {
                startRaiseToEarSensors(this.raiseChat);
            }
            return true;
        }
        if (!messageObject.isOut() && messageObject.isContentUnread()) {
            MessagesController.getInstance(messageObject.currentAccount).markMessageContentAsRead(messageObject);
        }
        boolean z6 = !this.playMusicAgain;
        MessageObject messageObject3 = this.playingMessageObject;
        if (messageObject3 != null) {
            boolean zSaveMusicPlaylistStateIfNeeded = ((messageObject3.isMusic() && messageObject.isVoice()) || messageObject.isRoundVideo() || messageObject.isVideo()) ? saveMusicPlaylistStateIfNeeded() : false;
            if (!this.playMusicAgain) {
                this.playingMessageObject.resetPlayingProgress();
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
            }
            z2 = zSaveMusicPlaylistStateIfNeeded;
            z6 = false;
        } else {
            z2 = false;
        }
        cleanupPlayer(z6, false);
        this.shouldSavePositionForCurrentAudio = null;
        this.lastSaveTime = 0L;
        this.playMusicAgain = false;
        this.seekToProgressPending = 0.0f;
        String str5 = messageObject.messageOwner.attachPath;
        try {
            if (str5 != null && str5.length() > 0) {
                File file3 = new File(messageObject.messageOwner.attachPath);
                zExists = file3.exists();
                if (zExists) {
                    file = file3;
                }
                if (file != null) {
                    pathToMessage = file;
                } else {
                    pathToMessage = FileLoader.getInstance(messageObject.currentAccount).getPathToMessage(messageObject.messageOwner);
                }
                if (SharedConfig.streamMedia || (!(messageObject.isMusic() || messageObject.isRoundVideo() || (messageObject.isVideo() && messageObject.canStreamVideo())) || messageObject.shouldEncryptPhotoOrVideo() || DialogObject.isEncryptedDialog(messageObject.getDialogId()))) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                if (pathToMessage == file && !(zExists = pathToMessage.exists()) && !z3) {
                    FileLoader.getInstance(messageObject.currentAccount).loadFile(messageObject.getDocument(), messageObject, 0, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 0);
                    this.downloadingCurrentMessage = true;
                    this.isPaused = false;
                    this.lastProgress = 0L;
                    this.audioInfo = null;
                    this.playingMessageObject = messageObject;
                    if (canStartMusicPlayerService()) {
                        try {
                            ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) MusicPlayerService.class));
                        } catch (Throwable th) {
                            FileLog.e(th);
                        }
                    } else {
                        ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class<?>) MusicPlayerService.class));
                    }
                    NotificationCenter.getInstance(this.playingMessageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
                    return true;
                }
                z4 = zExists;
                this.downloadingCurrentMessage = false;
                if (messageObject.isMusic()) {
                    checkIsNextMusicFileDownloaded(messageObject.currentAccount);
                } else {
                    checkIsNextVoiceFileDownloaded(messageObject.currentAccount);
                }
                aspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
                if (aspectRatioFrameLayout != null) {
                    this.isDrawingWasReady = false;
                    aspectRatioFrameLayout.setDrawingReady(false);
                }
                zIsVideo = messageObject.isVideo();
                z5 = z2;
                if (!messageObject.isRoundVideo() || zIsVideo) {
                    file2 = pathToMessage;
                    FileLoader.getInstance(messageObject.currentAccount).setLoadingVideoForPlayer(messageObject.getDocument(), true);
                    this.playerWasReady = false;
                    if (zIsVideo) {
                        str = "?account=";
                        str2 = "UTF-8";
                        boolean z7 = messageObject.messageOwner.peer_id.channel_id != 0 && messageObject.audioProgress <= 0.1f;
                        if (zIsVideo || messageObject.getDuration() > 30.0d) {
                            iArr = null;
                        } else {
                            iArr = new int[]{1};
                        }
                        if (!z5) {
                            clearPlaylist();
                        }
                        VideoPlayer videoPlayer3 = new VideoPlayer();
                        this.videoPlayer = videoPlayer3;
                        str3 = str;
                        videoPlayer3.setLooping(z);
                        int i4 = this.playerNum + 1;
                        this.playerNum = i4;
                        str4 = str2;
                        this.videoPlayer.setDelegate(new AnonymousClass9(i4, messageObject, iArr, z7));
                        this.currentAspectRatioFrameLayoutReady = false;
                        if (this.pipRoundVideoView == null || !MessagesController.getInstance(messageObject.currentAccount).isDialogVisible(messageObject.getDialogId(), messageObject.scheduled)) {
                            if (this.pipRoundVideoView == null) {
                                try {
                                    PipRoundVideoView pipRoundVideoView2 = new PipRoundVideoView();
                                    this.pipRoundVideoView = pipRoundVideoView2;
                                    pipRoundVideoView2.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$playMessage$20();
                                        }
                                    });
                                } catch (Exception unused) {
                                    this.pipRoundVideoView = null;
                                }
                            }
                            pipRoundVideoView = this.pipRoundVideoView;
                            if (pipRoundVideoView != null) {
                                this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                            }
                        } else {
                            TextureView textureView = this.currentTextureView;
                            if (textureView != null) {
                                this.videoPlayer.setTextureView(textureView);
                            }
                        }
                        if (z4) {
                            if (!messageObject.mediaExists && file2 != file) {
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda18
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MessageObject messageObject4 = messageObject;
                                        NotificationCenter.getInstance(messageObject4.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject4.getDocument()), file2);
                                    }
                                });
                                if (AyuConfig.saveDeletedMessages && (message = messageObject.messageOwner) != null && message.ayuDeleted) {
                                    AyuMessagesController.getInstance(messageObject.currentAccount).onMediaDownloaded(new SaveMessageRequest(messageObject.currentAccount, message));
                                }
                            }
                            this.videoPlayer.preparePlayer(Uri.fromFile(file2), "other");
                        } else {
                            try {
                                int fileReference = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                                TLRPC.Document document = messageObject.getDocument();
                                StringBuilder sb = new StringBuilder();
                                sb.append(str3);
                                sb.append(messageObject.currentAccount);
                                sb.append("&id=");
                                sb.append(document.id);
                                sb.append("&hash=");
                                sb.append(document.access_hash);
                                sb.append("&dc=");
                                sb.append(document.dc_id);
                                sb.append("&size=");
                                sb.append(document.size);
                                sb.append("&mime=");
                                sb.append(URLEncoder.encode(document.mime_type, str4));
                                sb.append("&rid=");
                                sb.append(fileReference);
                                sb.append("&name=");
                                sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(document), str4));
                                sb.append("&reference=");
                                bArr = document.file_reference;
                                if (bArr != null) {
                                    bArr = new byte[0];
                                }
                                sb.append(Utilities.bytesToHex(bArr));
                                this.videoPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb.toString()), "other");
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                        if (messageObject.isRoundVideo()) {
                            VideoPlayer videoPlayer4 = this.videoPlayer;
                            if (this.useFrontSpeaker) {
                                i = 0;
                            } else {
                                i = 3;
                            }
                            videoPlayer4.setStreamType(i);
                            if (Math.abs(this.currentPlaybackSpeed - 1.0f) > 0.001f) {
                                this.videoPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                            }
                            f = messageObject.forceSeekTo;
                            if (f >= r14) {
                                this.seekToProgressPending = f;
                                messageObject.audioProgress = f;
                                messageObject.forceSeekTo = -1.0f;
                            }
                        } else {
                            this.videoPlayer.setStreamType(3);
                        }
                    } else {
                        str = "?account=";
                        str2 = "UTF-8";
                    }
                    if (zIsVideo) {
                        iArr = null;
                    } else {
                        iArr = null;
                    }
                    if (!z5) {
                        clearPlaylist();
                    }
                    VideoPlayer videoPlayer5 = new VideoPlayer();
                    this.videoPlayer = videoPlayer5;
                    str3 = str;
                    videoPlayer5.setLooping(z);
                    int i5 = this.playerNum + 1;
                    this.playerNum = i5;
                    str4 = str2;
                    this.videoPlayer.setDelegate(new AnonymousClass9(i5, messageObject, iArr, z7));
                    this.currentAspectRatioFrameLayoutReady = false;
                    if (this.pipRoundVideoView == null) {
                        if (this.pipRoundVideoView == null) {
                            PipRoundVideoView pipRoundVideoView3 = new PipRoundVideoView();
                            this.pipRoundVideoView = pipRoundVideoView3;
                            pipRoundVideoView3.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$playMessage$20();
                                }
                            });
                        }
                        pipRoundVideoView = this.pipRoundVideoView;
                        if (pipRoundVideoView != null) {
                            this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                        }
                    } else {
                        if (this.pipRoundVideoView == null) {
                            PipRoundVideoView pipRoundVideoView4 = new PipRoundVideoView();
                            this.pipRoundVideoView = pipRoundVideoView4;
                            pipRoundVideoView4.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$playMessage$20();
                                }
                            });
                        }
                        pipRoundVideoView = this.pipRoundVideoView;
                        if (pipRoundVideoView != null) {
                            this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                        }
                    }
                    if (z4) {
                        if (!messageObject.mediaExists) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda18
                                @Override // java.lang.Runnable
                                public final void run() {
                                    MessageObject messageObject4 = messageObject;
                                    NotificationCenter.getInstance(messageObject4.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject4.getDocument()), file2);
                                }
                            });
                            if (AyuConfig.saveDeletedMessages) {
                                AyuMessagesController.getInstance(messageObject.currentAccount).onMediaDownloaded(new SaveMessageRequest(messageObject.currentAccount, message));
                            }
                        }
                        this.videoPlayer.preparePlayer(Uri.fromFile(file2), "other");
                    } else {
                        int fileReference2 = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                        TLRPC.Document document2 = messageObject.getDocument();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str3);
                        sb2.append(messageObject.currentAccount);
                        sb2.append("&id=");
                        sb2.append(document2.id);
                        sb2.append("&hash=");
                        sb2.append(document2.access_hash);
                        sb2.append("&dc=");
                        sb2.append(document2.dc_id);
                        sb2.append("&size=");
                        sb2.append(document2.size);
                        sb2.append("&mime=");
                        sb2.append(URLEncoder.encode(document2.mime_type, str4));
                        sb2.append("&rid=");
                        sb2.append(fileReference2);
                        sb2.append("&name=");
                        sb2.append(URLEncoder.encode(FileLoader.getDocumentFileName(document2), str4));
                        sb2.append("&reference=");
                        bArr = document2.file_reference;
                        if (bArr != null) {
                            bArr = new byte[0];
                        }
                        sb2.append(Utilities.bytesToHex(bArr));
                        this.videoPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb2.toString()), "other");
                    }
                    if (messageObject.isRoundVideo()) {
                        VideoPlayer videoPlayer6 = this.videoPlayer;
                        if (this.useFrontSpeaker) {
                            i = 0;
                        } else {
                            i = 3;
                        }
                        videoPlayer6.setStreamType(i);
                        if (Math.abs(this.currentPlaybackSpeed - 1.0f) > 0.001f) {
                            this.videoPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                        }
                        f = messageObject.forceSeekTo;
                        if (f >= r14) {
                            this.seekToProgressPending = f;
                            messageObject.audioProgress = f;
                            messageObject.forceSeekTo = -1.0f;
                        }
                    } else {
                        this.videoPlayer.setStreamType(3);
                    }
                } else {
                    PipRoundVideoView pipRoundVideoView5 = this.pipRoundVideoView;
                    if (pipRoundVideoView5 != null) {
                        pipRoundVideoView5.close(true);
                        this.pipRoundVideoView = null;
                    }
                    try {
                        VideoPlayer videoPlayer7 = new VideoPlayer();
                        this.audioPlayer = videoPlayer7;
                        final int i6 = this.playerNum + 1;
                        this.playerNum = i6;
                        videoPlayer7.setDelegate(new VideoPlayer.VideoPlayerDelegate() { // from class: org.telegram.messenger.MediaController.10
                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public void onError(VideoPlayer videoPlayer8, Exception exc) {
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public void onRenderedFirstFrame() {
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public /* bridge */ /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
                                VideoPlayer.VideoPlayerDelegate.CC.$default$onRenderedFirstFrame(this, eventTime);
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public /* bridge */ /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
                                VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekFinished(this, eventTime);
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public /* bridge */ /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
                                VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekStarted(this, eventTime);
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
                                return false;
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public void onVideoSizeChanged(int i7, int i8, int i9, float f2) {
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                            public void onStateChanged(boolean z8, int i7) {
                                if (i6 != MediaController.this.playerNum) {
                                    return;
                                }
                                if (i7 == 4 || ((i7 == 1 || i7 == 2) && z8 && messageObject.audioProgress >= 0.999f)) {
                                    MessageObject messageObject4 = messageObject;
                                    messageObject4.audioProgress = 1.0f;
                                    NotificationCenter.getInstance(messageObject4.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(messageObject.getId()), 0);
                                    if (!MediaController.this.restoreMusicPlaylistState()) {
                                        if (!MediaController.this.playlist.isEmpty() && (MediaController.this.playlist.size() > 1 || !messageObject.isVoice())) {
                                            MediaController.this.playNextMessageWithoutOrder(true);
                                        } else {
                                            MediaController mediaController = MediaController.this;
                                            mediaController.cleanupPlayer(true, mediaController.hasNoNextVoiceOrRoundVideoMessage(), messageObject.isVoice(), false);
                                        }
                                    }
                                } else if (MediaController.this.audioPlayer != null && MediaController.this.seekToProgressPending != 0.0f && (i7 == 3 || i7 == 1)) {
                                    long duration3 = (int) (MediaController.this.audioPlayer.getDuration() * MediaController.this.seekToProgressPending);
                                    MediaController.this.audioPlayer.seekTo(duration3);
                                    MediaController.this.lastProgress = duration3;
                                    MediaController.this.seekToProgressPending = 0.0f;
                                }
                                if (MediaController.this.audioPlayer == null || !CastSync.isActive()) {
                                    return;
                                }
                                MediaController.this.audioPlayer.setMute(true);
                            }
                        });
                        this.audioPlayer.setAudioVisualizerDelegate(new VideoPlayer.AudioVisualizerDelegate() { // from class: org.telegram.messenger.MediaController.11
                            @Override // org.telegram.ui.Components.VideoPlayer.AudioVisualizerDelegate
                            public void onVisualizerUpdate(boolean z8, boolean z9, float[] fArr) {
                                Theme.getCurrentAudiVisualizerDrawable().setWaveform(z8, z9, fArr);
                            }

                            @Override // org.telegram.ui.Components.VideoPlayer.AudioVisualizerDelegate
                            public boolean needUpdate() {
                                return Theme.getCurrentAudiVisualizerDrawable().getParentView() != null;
                            }
                        });
                        if (z4) {
                            if (!messageObject.mediaExists && pathToMessage != file) {
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda19
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MessageObject messageObject4 = messageObject;
                                        NotificationCenter.getInstance(messageObject4.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject4.getDocument()), pathToMessage);
                                    }
                                });
                                if (AyuConfig.saveDeletedMessages && (message2 = messageObject.messageOwner) != null && message2.ayuDeleted) {
                                    AyuMessagesController.getInstance(messageObject.currentAccount).onMediaDownloaded(new SaveMessageRequest(messageObject.currentAccount, message2));
                                }
                            }
                            this.audioPlayer.preparePlayer(Uri.fromFile(pathToMessage), "other");
                            this.isStreamingCurrentAudio = false;
                        } else {
                            int fileReference3 = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                            TLRPC.Document document3 = messageObject.getDocument();
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("?account=");
                            sb3.append(messageObject.currentAccount);
                            sb3.append("&id=");
                            sb3.append(document3.id);
                            sb3.append("&hash=");
                            sb3.append(document3.access_hash);
                            sb3.append("&dc=");
                            sb3.append(document3.dc_id);
                            sb3.append("&size=");
                            sb3.append(document3.size);
                            sb3.append("&mime=");
                            sb3.append(URLEncoder.encode(document3.mime_type, "UTF-8"));
                            sb3.append("&rid=");
                            sb3.append(fileReference3);
                            sb3.append("&name=");
                            sb3.append(URLEncoder.encode(FileLoader.getDocumentFileName(document3), "UTF-8"));
                            sb3.append("&reference=");
                            byte[] bArr2 = document3.file_reference;
                            if (bArr2 == null) {
                                bArr2 = new byte[0];
                            }
                            sb3.append(Utilities.bytesToHex(bArr2));
                            this.audioPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb3.toString()), "other");
                            this.isStreamingCurrentAudio = true;
                        }
                        if (messageObject.isVoice()) {
                            String fileName = messageObject.getFileName();
                            if (fileName != null && messageObject.getDuration() >= 300.0d) {
                                float f2 = ApplicationLoader.applicationContext.getSharedPreferences("media_saved_pos", 0).getFloat(fileName, -1.0f);
                                if (f2 > r14 && f2 < 0.99f) {
                                    this.seekToProgressPending = f2;
                                    messageObject.audioProgress = f2;
                                }
                                this.shouldSavePositionForCurrentAudio = fileName;
                            }
                            if (Math.abs(this.currentPlaybackSpeed - 1.0f) > 0.001f) {
                                this.audioPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                            }
                            this.audioInfo = null;
                            if (!z5) {
                                clearPlaylist();
                            }
                        } else {
                            try {
                                this.audioInfo = AudioInfo.getAudioInfo(pathToMessage);
                            } catch (Exception e2) {
                                FileLog.e(e2);
                            }
                            String fileName2 = messageObject.getFileName();
                            if (!TextUtils.isEmpty(fileName2) && messageObject.getDuration() >= 600.0d) {
                                float f3 = ApplicationLoader.applicationContext.getSharedPreferences("media_saved_pos", 0).getFloat(fileName2, -1.0f);
                                if (f3 > r14 && f3 < 0.999f) {
                                    this.seekToProgressPending = f3;
                                    messageObject.audioProgress = f3;
                                }
                                this.shouldSavePositionForCurrentAudio = fileName2;
                                if (Math.abs(this.currentMusicPlaybackSpeed - 1.0f) > 0.001f) {
                                    this.audioPlayer.setPlaybackSpeed(Math.round(this.currentMusicPlaybackSpeed * 10.0f) / 10.0f);
                                }
                            }
                        }
                        float f4 = messageObject.forceSeekTo;
                        if (f4 >= r14) {
                            this.seekToProgressPending = f4;
                            messageObject.audioProgress = f4;
                            messageObject.forceSeekTo = -1.0f;
                        }
                        this.audioPlayer.setStreamType(this.useFrontSpeaker ? 0 : 3);
                        this.audioPlayer.play();
                        if (!messageObject.isVoice()) {
                            ValueAnimator valueAnimator = this.audioVolumeAnimator;
                            if (valueAnimator != null) {
                                valueAnimator.removeAllListeners();
                                this.audioVolumeAnimator.cancel();
                            }
                            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.audioVolume, 1.0f);
                            this.audioVolumeAnimator = valueAnimatorOfFloat;
                            valueAnimatorOfFloat.addUpdateListener(this.audioVolumeUpdateListener);
                            this.audioVolumeAnimator.setDuration(300L);
                            this.audioVolumeAnimator.start();
                        } else {
                            this.audioVolume = 1.0f;
                            setPlayerVolume();
                        }
                    } catch (Exception e3) {
                        FileLog.e(e3);
                        NotificationCenter notificationCenter = NotificationCenter.getInstance(messageObject.currentAccount);
                        int i7 = NotificationCenter.messagePlayingPlayStateChanged;
                        MessageObject messageObject4 = this.playingMessageObject;
                        notificationCenter.lambda$postNotificationNameOnUIThread$1(i7, Integer.valueOf(messageObject4 != null ? messageObject4.getId() : 0));
                        VideoPlayer videoPlayer8 = this.audioPlayer;
                        if (videoPlayer8 != null) {
                            videoPlayer8.releasePlayer(true);
                            this.audioPlayer = null;
                            Theme.unrefAudioVisualizeDrawable(this.playingMessageObject);
                            this.isPaused = false;
                            this.playingMessageObject = null;
                            this.downloadingCurrentMessage = false;
                        }
                        return false;
                    }
                }
                checkAudioFocus(messageObject, true);
                setPlayerVolume();
                this.isPaused = false;
                this.lastProgress = 0L;
                this.playingMessageObject = messageObject;
                if (!SharedConfig.enabledRaiseTo(true)) {
                    startRaiseToEarSensors(this.raiseChat);
                }
                if (!ApplicationLoader.mainInterfacePaused || (wakeLock = this.proximityWakeLock) == null || wakeLock.isHeld() || !(this.playingMessageObject.isVoice() || this.playingMessageObject.isRoundVideo())) {
                    c = 0;
                } else {
                    c = 0;
                    SharedConfig.enabledRaiseTo(false);
                }
                startProgressTimer(this.playingMessageObject);
                NotificationCenter notificationCenter2 = NotificationCenter.getInstance(messageObject.currentAccount);
                int i8 = NotificationCenter.messagePlayingDidStart;
                Object[] objArr = new Object[2];
                objArr[c] = messageObject;
                objArr[1] = messageObject3;
                notificationCenter2.lambda$postNotificationNameOnUIThread$1(i8, objArr);
                videoPlayer = this.videoPlayer;
                if (videoPlayer != null) {
                    try {
                        if (this.playingMessageObject.audioProgress != r14) {
                            duration = videoPlayer.getDuration();
                            if (duration == -9223372036854775807L) {
                                duration = ((long) this.playingMessageObject.getDuration()) * 1000;
                            }
                            messageObject2 = this.playingMessageObject;
                            i2 = (int) (duration * messageObject2.audioProgress);
                            i3 = messageObject2.audioProgressMs;
                            if (i3 != 0) {
                                messageObject2.audioProgressMs = 0;
                                i2 = i3;
                            }
                            this.videoPlayer.seekTo(i2);
                        }
                    } catch (Exception e4) {
                        MessageObject messageObject5 = this.playingMessageObject;
                        messageObject5.audioProgress = 0.0f;
                        messageObject5.audioProgressSec = 0;
                        NotificationCenter.getInstance(messageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
                        FileLog.e(e4);
                    }
                    this.videoPlayer.play();
                } else {
                    videoPlayer2 = this.audioPlayer;
                    if (videoPlayer2 != null) {
                        try {
                            if (this.playingMessageObject.audioProgress != 0.0f) {
                                duration2 = videoPlayer2.getDuration();
                                if (duration2 == -9223372036854775807L) {
                                    duration2 = ((long) this.playingMessageObject.getDuration()) * 1000;
                                }
                                j = (int) (duration2 * this.playingMessageObject.audioProgress);
                                this.audioPlayer.seekTo(j);
                                if (!this.ignorePlayerUpdate) {
                                    CastSync.seekTo(j);
                                }
                            }
                        } catch (Exception e5) {
                            this.playingMessageObject.resetPlayingProgress();
                            NotificationCenter.getInstance(messageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
                            FileLog.e(e5);
                        }
                    }
                }
                if (canStartMusicPlayerService()) {
                    try {
                        ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) MusicPlayerService.class));
                    } catch (Throwable th2) {
                        FileLog.e(th2);
                    }
                } else {
                    ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class<?>) MusicPlayerService.class));
                }
                CastSync.check(1);
                if (!this.ignorePlayerUpdate) {
                    return true;
                }
                if (ChromecastController.getInstance().isCasting()) {
                    ChromecastController.getInstance().setCurrentMediaAndCastIfNeeded(getCurrentChromecastMedia());
                }
                CastSync.setPlaying(true);
                return true;
            }
            zExists = false;
            CastSync.check(1);
            if (!this.ignorePlayerUpdate) {
                return true;
            }
            if (ChromecastController.getInstance().isCasting()) {
                ChromecastController.getInstance().setCurrentMediaAndCastIfNeeded(getCurrentChromecastMedia());
            }
            CastSync.setPlaying(true);
            return true;
        } catch (Exception e6) {
            FileLog.e(e6);
            return true;
        }
        file = null;
        if (file != null) {
            pathToMessage = file;
        } else {
            pathToMessage = FileLoader.getInstance(messageObject.currentAccount).getPathToMessage(messageObject.messageOwner);
        }
        if (SharedConfig.streamMedia) {
            z3 = false;
        } else {
            z3 = false;
        }
        if (pathToMessage == file) {
        }
        z4 = zExists;
        this.downloadingCurrentMessage = false;
        if (messageObject.isMusic()) {
            checkIsNextMusicFileDownloaded(messageObject.currentAccount);
        } else {
            checkIsNextVoiceFileDownloaded(messageObject.currentAccount);
        }
        aspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        if (aspectRatioFrameLayout != null) {
            this.isDrawingWasReady = false;
            aspectRatioFrameLayout.setDrawingReady(false);
        }
        zIsVideo = messageObject.isVideo();
        z5 = z2;
        if (!messageObject.isRoundVideo()) {
            file2 = pathToMessage;
            FileLoader.getInstance(messageObject.currentAccount).setLoadingVideoForPlayer(messageObject.getDocument(), true);
            this.playerWasReady = false;
            if (zIsVideo) {
                str = "?account=";
                str2 = "UTF-8";
                if (messageObject.messageOwner.peer_id.channel_id != 0) {
                }
                if (zIsVideo) {
                    iArr = null;
                } else {
                    iArr = null;
                }
                if (!z5) {
                    clearPlaylist();
                }
                VideoPlayer videoPlayer9 = new VideoPlayer();
                this.videoPlayer = videoPlayer9;
                str3 = str;
                videoPlayer9.setLooping(z);
                int i9 = this.playerNum + 1;
                this.playerNum = i9;
                str4 = str2;
                this.videoPlayer.setDelegate(new AnonymousClass9(i9, messageObject, iArr, z7));
                this.currentAspectRatioFrameLayoutReady = false;
                if (this.pipRoundVideoView == null) {
                    if (this.pipRoundVideoView == null) {
                        PipRoundVideoView pipRoundVideoView6 = new PipRoundVideoView();
                        this.pipRoundVideoView = pipRoundVideoView6;
                        pipRoundVideoView6.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$playMessage$20();
                            }
                        });
                    }
                    pipRoundVideoView = this.pipRoundVideoView;
                    if (pipRoundVideoView != null) {
                        this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                    }
                } else {
                    if (this.pipRoundVideoView == null) {
                        PipRoundVideoView pipRoundVideoView7 = new PipRoundVideoView();
                        this.pipRoundVideoView = pipRoundVideoView7;
                        pipRoundVideoView7.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$playMessage$20();
                            }
                        });
                    }
                    pipRoundVideoView = this.pipRoundVideoView;
                    if (pipRoundVideoView != null) {
                        this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                    }
                }
                if (z4) {
                    if (!messageObject.mediaExists) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda18
                            @Override // java.lang.Runnable
                            public final void run() {
                                MessageObject messageObject6 = messageObject;
                                NotificationCenter.getInstance(messageObject6.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject6.getDocument()), file2);
                            }
                        });
                        if (AyuConfig.saveDeletedMessages) {
                            AyuMessagesController.getInstance(messageObject.currentAccount).onMediaDownloaded(new SaveMessageRequest(messageObject.currentAccount, message));
                        }
                    }
                    this.videoPlayer.preparePlayer(Uri.fromFile(file2), "other");
                } else {
                    int fileReference4 = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                    TLRPC.Document document4 = messageObject.getDocument();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str3);
                    sb4.append(messageObject.currentAccount);
                    sb4.append("&id=");
                    sb4.append(document4.id);
                    sb4.append("&hash=");
                    sb4.append(document4.access_hash);
                    sb4.append("&dc=");
                    sb4.append(document4.dc_id);
                    sb4.append("&size=");
                    sb4.append(document4.size);
                    sb4.append("&mime=");
                    sb4.append(URLEncoder.encode(document4.mime_type, str4));
                    sb4.append("&rid=");
                    sb4.append(fileReference4);
                    sb4.append("&name=");
                    sb4.append(URLEncoder.encode(FileLoader.getDocumentFileName(document4), str4));
                    sb4.append("&reference=");
                    bArr = document4.file_reference;
                    if (bArr != null) {
                        bArr = new byte[0];
                    }
                    sb4.append(Utilities.bytesToHex(bArr));
                    this.videoPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb4.toString()), "other");
                }
                if (messageObject.isRoundVideo()) {
                    VideoPlayer videoPlayer10 = this.videoPlayer;
                    if (this.useFrontSpeaker) {
                        i = 0;
                    } else {
                        i = 3;
                    }
                    videoPlayer10.setStreamType(i);
                    if (Math.abs(this.currentPlaybackSpeed - 1.0f) > 0.001f) {
                        this.videoPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                    }
                    f = messageObject.forceSeekTo;
                    if (f >= r14) {
                        this.seekToProgressPending = f;
                        messageObject.audioProgress = f;
                        messageObject.forceSeekTo = -1.0f;
                    }
                } else {
                    this.videoPlayer.setStreamType(3);
                }
            } else {
                str = "?account=";
                str2 = "UTF-8";
            }
            if (zIsVideo) {
                iArr = null;
            } else {
                iArr = null;
            }
            if (!z5) {
                clearPlaylist();
            }
            VideoPlayer videoPlayer11 = new VideoPlayer();
            this.videoPlayer = videoPlayer11;
            str3 = str;
            videoPlayer11.setLooping(z);
            int i10 = this.playerNum + 1;
            this.playerNum = i10;
            str4 = str2;
            this.videoPlayer.setDelegate(new AnonymousClass9(i10, messageObject, iArr, z7));
            this.currentAspectRatioFrameLayoutReady = false;
            if (this.pipRoundVideoView == null) {
                if (this.pipRoundVideoView == null) {
                    PipRoundVideoView pipRoundVideoView8 = new PipRoundVideoView();
                    this.pipRoundVideoView = pipRoundVideoView8;
                    pipRoundVideoView8.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$playMessage$20();
                        }
                    });
                }
                pipRoundVideoView = this.pipRoundVideoView;
                if (pipRoundVideoView != null) {
                    this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                }
            } else {
                if (this.pipRoundVideoView == null) {
                    PipRoundVideoView pipRoundVideoView9 = new PipRoundVideoView();
                    this.pipRoundVideoView = pipRoundVideoView9;
                    pipRoundVideoView9.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$playMessage$20();
                        }
                    });
                }
                pipRoundVideoView = this.pipRoundVideoView;
                if (pipRoundVideoView != null) {
                    this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                }
            }
            if (z4) {
                if (!messageObject.mediaExists) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda18
                        @Override // java.lang.Runnable
                        public final void run() {
                            MessageObject messageObject6 = messageObject;
                            NotificationCenter.getInstance(messageObject6.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject6.getDocument()), file2);
                        }
                    });
                    if (AyuConfig.saveDeletedMessages) {
                        AyuMessagesController.getInstance(messageObject.currentAccount).onMediaDownloaded(new SaveMessageRequest(messageObject.currentAccount, message));
                    }
                }
                this.videoPlayer.preparePlayer(Uri.fromFile(file2), "other");
            } else {
                int fileReference5 = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                TLRPC.Document document5 = messageObject.getDocument();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str3);
                sb5.append(messageObject.currentAccount);
                sb5.append("&id=");
                sb5.append(document5.id);
                sb5.append("&hash=");
                sb5.append(document5.access_hash);
                sb5.append("&dc=");
                sb5.append(document5.dc_id);
                sb5.append("&size=");
                sb5.append(document5.size);
                sb5.append("&mime=");
                sb5.append(URLEncoder.encode(document5.mime_type, str4));
                sb5.append("&rid=");
                sb5.append(fileReference5);
                sb5.append("&name=");
                sb5.append(URLEncoder.encode(FileLoader.getDocumentFileName(document5), str4));
                sb5.append("&reference=");
                bArr = document5.file_reference;
                if (bArr != null) {
                    bArr = new byte[0];
                }
                sb5.append(Utilities.bytesToHex(bArr));
                this.videoPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb5.toString()), "other");
            }
            if (messageObject.isRoundVideo()) {
                VideoPlayer videoPlayer12 = this.videoPlayer;
                if (this.useFrontSpeaker) {
                    i = 0;
                } else {
                    i = 3;
                }
                videoPlayer12.setStreamType(i);
                if (Math.abs(this.currentPlaybackSpeed - 1.0f) > 0.001f) {
                    this.videoPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                }
                f = messageObject.forceSeekTo;
                if (f >= r14) {
                    this.seekToProgressPending = f;
                    messageObject.audioProgress = f;
                    messageObject.forceSeekTo = -1.0f;
                }
            } else {
                this.videoPlayer.setStreamType(3);
            }
        } else {
            file2 = pathToMessage;
            FileLoader.getInstance(messageObject.currentAccount).setLoadingVideoForPlayer(messageObject.getDocument(), true);
            this.playerWasReady = false;
            if (zIsVideo) {
                str = "?account=";
                str2 = "UTF-8";
                if (messageObject.messageOwner.peer_id.channel_id != 0) {
                }
                if (zIsVideo) {
                    iArr = null;
                } else {
                    iArr = null;
                }
                if (!z5) {
                    clearPlaylist();
                }
                VideoPlayer videoPlayer13 = new VideoPlayer();
                this.videoPlayer = videoPlayer13;
                str3 = str;
                videoPlayer13.setLooping(z);
                int i11 = this.playerNum + 1;
                this.playerNum = i11;
                str4 = str2;
                this.videoPlayer.setDelegate(new AnonymousClass9(i11, messageObject, iArr, z7));
                this.currentAspectRatioFrameLayoutReady = false;
                if (this.pipRoundVideoView == null) {
                    if (this.pipRoundVideoView == null) {
                        PipRoundVideoView pipRoundVideoView10 = new PipRoundVideoView();
                        this.pipRoundVideoView = pipRoundVideoView10;
                        pipRoundVideoView10.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$playMessage$20();
                            }
                        });
                    }
                    pipRoundVideoView = this.pipRoundVideoView;
                    if (pipRoundVideoView != null) {
                        this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                    }
                } else {
                    if (this.pipRoundVideoView == null) {
                        PipRoundVideoView pipRoundVideoView11 = new PipRoundVideoView();
                        this.pipRoundVideoView = pipRoundVideoView11;
                        pipRoundVideoView11.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$playMessage$20();
                            }
                        });
                    }
                    pipRoundVideoView = this.pipRoundVideoView;
                    if (pipRoundVideoView != null) {
                        this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                    }
                }
                if (z4) {
                    if (!messageObject.mediaExists) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda18
                            @Override // java.lang.Runnable
                            public final void run() {
                                MessageObject messageObject6 = messageObject;
                                NotificationCenter.getInstance(messageObject6.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject6.getDocument()), file2);
                            }
                        });
                        if (AyuConfig.saveDeletedMessages) {
                            AyuMessagesController.getInstance(messageObject.currentAccount).onMediaDownloaded(new SaveMessageRequest(messageObject.currentAccount, message));
                        }
                    }
                    this.videoPlayer.preparePlayer(Uri.fromFile(file2), "other");
                } else {
                    int fileReference6 = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                    TLRPC.Document document6 = messageObject.getDocument();
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str3);
                    sb6.append(messageObject.currentAccount);
                    sb6.append("&id=");
                    sb6.append(document6.id);
                    sb6.append("&hash=");
                    sb6.append(document6.access_hash);
                    sb6.append("&dc=");
                    sb6.append(document6.dc_id);
                    sb6.append("&size=");
                    sb6.append(document6.size);
                    sb6.append("&mime=");
                    sb6.append(URLEncoder.encode(document6.mime_type, str4));
                    sb6.append("&rid=");
                    sb6.append(fileReference6);
                    sb6.append("&name=");
                    sb6.append(URLEncoder.encode(FileLoader.getDocumentFileName(document6), str4));
                    sb6.append("&reference=");
                    bArr = document6.file_reference;
                    if (bArr != null) {
                        bArr = new byte[0];
                    }
                    sb6.append(Utilities.bytesToHex(bArr));
                    this.videoPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb6.toString()), "other");
                }
                if (messageObject.isRoundVideo()) {
                    VideoPlayer videoPlayer14 = this.videoPlayer;
                    if (this.useFrontSpeaker) {
                        i = 0;
                    } else {
                        i = 3;
                    }
                    videoPlayer14.setStreamType(i);
                    if (Math.abs(this.currentPlaybackSpeed - 1.0f) > 0.001f) {
                        this.videoPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                    }
                    f = messageObject.forceSeekTo;
                    if (f >= r14) {
                        this.seekToProgressPending = f;
                        messageObject.audioProgress = f;
                        messageObject.forceSeekTo = -1.0f;
                    }
                } else {
                    this.videoPlayer.setStreamType(3);
                }
            } else {
                str = "?account=";
                str2 = "UTF-8";
            }
            if (zIsVideo) {
                iArr = null;
            } else {
                iArr = null;
            }
            if (!z5) {
                clearPlaylist();
            }
            VideoPlayer videoPlayer15 = new VideoPlayer();
            this.videoPlayer = videoPlayer15;
            str3 = str;
            videoPlayer15.setLooping(z);
            int i12 = this.playerNum + 1;
            this.playerNum = i12;
            str4 = str2;
            this.videoPlayer.setDelegate(new AnonymousClass9(i12, messageObject, iArr, z7));
            this.currentAspectRatioFrameLayoutReady = false;
            if (this.pipRoundVideoView == null) {
                if (this.pipRoundVideoView == null) {
                    PipRoundVideoView pipRoundVideoView12 = new PipRoundVideoView();
                    this.pipRoundVideoView = pipRoundVideoView12;
                    pipRoundVideoView12.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$playMessage$20();
                        }
                    });
                }
                pipRoundVideoView = this.pipRoundVideoView;
                if (pipRoundVideoView != null) {
                    this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                }
            } else {
                if (this.pipRoundVideoView == null) {
                    PipRoundVideoView pipRoundVideoView13 = new PipRoundVideoView();
                    this.pipRoundVideoView = pipRoundVideoView13;
                    pipRoundVideoView13.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$playMessage$20();
                        }
                    });
                }
                pipRoundVideoView = this.pipRoundVideoView;
                if (pipRoundVideoView != null) {
                    this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                }
            }
            if (z4) {
                if (!messageObject.mediaExists) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda18
                        @Override // java.lang.Runnable
                        public final void run() {
                            MessageObject messageObject6 = messageObject;
                            NotificationCenter.getInstance(messageObject6.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject6.getDocument()), file2);
                        }
                    });
                    if (AyuConfig.saveDeletedMessages) {
                        AyuMessagesController.getInstance(messageObject.currentAccount).onMediaDownloaded(new SaveMessageRequest(messageObject.currentAccount, message));
                    }
                }
                this.videoPlayer.preparePlayer(Uri.fromFile(file2), "other");
            } else {
                int fileReference7 = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                TLRPC.Document document7 = messageObject.getDocument();
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str3);
                sb7.append(messageObject.currentAccount);
                sb7.append("&id=");
                sb7.append(document7.id);
                sb7.append("&hash=");
                sb7.append(document7.access_hash);
                sb7.append("&dc=");
                sb7.append(document7.dc_id);
                sb7.append("&size=");
                sb7.append(document7.size);
                sb7.append("&mime=");
                sb7.append(URLEncoder.encode(document7.mime_type, str4));
                sb7.append("&rid=");
                sb7.append(fileReference7);
                sb7.append("&name=");
                sb7.append(URLEncoder.encode(FileLoader.getDocumentFileName(document7), str4));
                sb7.append("&reference=");
                bArr = document7.file_reference;
                if (bArr != null) {
                    bArr = new byte[0];
                }
                sb7.append(Utilities.bytesToHex(bArr));
                this.videoPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb7.toString()), "other");
            }
            if (messageObject.isRoundVideo()) {
                VideoPlayer videoPlayer16 = this.videoPlayer;
                if (this.useFrontSpeaker) {
                    i = 0;
                } else {
                    i = 3;
                }
                videoPlayer16.setStreamType(i);
                if (Math.abs(this.currentPlaybackSpeed - 1.0f) > 0.001f) {
                    this.videoPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                }
                f = messageObject.forceSeekTo;
                if (f >= r14) {
                    this.seekToProgressPending = f;
                    messageObject.audioProgress = f;
                    messageObject.forceSeekTo = -1.0f;
                }
            } else {
                this.videoPlayer.setStreamType(3);
            }
        }
        checkAudioFocus(messageObject, true);
        setPlayerVolume();
        this.isPaused = false;
        this.lastProgress = 0L;
        this.playingMessageObject = messageObject;
        if (!SharedConfig.enabledRaiseTo(true)) {
            startRaiseToEarSensors(this.raiseChat);
        }
        if (ApplicationLoader.mainInterfacePaused) {
            c = 0;
        } else {
            c = 0;
        }
        startProgressTimer(this.playingMessageObject);
        NotificationCenter notificationCenter3 = NotificationCenter.getInstance(messageObject.currentAccount);
        int i13 = NotificationCenter.messagePlayingDidStart;
        Object[] objArr2 = new Object[2];
        objArr2[c] = messageObject;
        objArr2[1] = messageObject3;
        notificationCenter3.lambda$postNotificationNameOnUIThread$1(i13, objArr2);
        videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            if (this.playingMessageObject.audioProgress != r14) {
                duration = videoPlayer.getDuration();
                if (duration == -9223372036854775807L) {
                    duration = ((long) this.playingMessageObject.getDuration()) * 1000;
                }
                messageObject2 = this.playingMessageObject;
                i2 = (int) (duration * messageObject2.audioProgress);
                i3 = messageObject2.audioProgressMs;
                if (i3 != 0) {
                    messageObject2.audioProgressMs = 0;
                    i2 = i3;
                }
                this.videoPlayer.seekTo(i2);
            }
            this.videoPlayer.play();
        } else {
            videoPlayer2 = this.audioPlayer;
            if (videoPlayer2 != null) {
                if (this.playingMessageObject.audioProgress != 0.0f) {
                    duration2 = videoPlayer2.getDuration();
                    if (duration2 == -9223372036854775807L) {
                        duration2 = ((long) this.playingMessageObject.getDuration()) * 1000;
                    }
                    j = (int) (duration2 * this.playingMessageObject.audioProgress);
                    this.audioPlayer.seekTo(j);
                    if (!this.ignorePlayerUpdate) {
                        CastSync.seekTo(j);
                    }
                }
            }
        }
        if (canStartMusicPlayerService()) {
            ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) MusicPlayerService.class));
        } else {
            ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class<?>) MusicPlayerService.class));
        }
    }

    /* JADX INFO: renamed from: org.telegram.messenger.MediaController$9, reason: invalid class name */
    class AnonymousClass9 implements VideoPlayer.VideoPlayerDelegate {
        final /* synthetic */ boolean val$destroyAtEnd;
        final /* synthetic */ MessageObject val$messageObject;
        final /* synthetic */ int[] val$playCount;
        final /* synthetic */ int val$tag;

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onRenderedFirstFrame(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekFinished(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekStarted(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        AnonymousClass9(int i, MessageObject messageObject, int[] iArr, boolean z) {
            this.val$tag = i;
            this.val$messageObject = messageObject;
            this.val$playCount = iArr;
            this.val$destroyAtEnd = z;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onStateChanged(boolean z, int i) {
            if (this.val$tag != MediaController.this.playerNum) {
                return;
            }
            MediaController.this.updateVideoState(this.val$messageObject, this.val$playCount, this.val$destroyAtEnd, z, i);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
            FileLog.e(exc);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onVideoSizeChanged(int i, int i2, int i3, float f) {
            MediaController.this.currentAspectRatioFrameLayoutRotation = i3;
            if (i3 != 90 && i3 != 270) {
                i2 = i;
                i = i2;
            }
            MediaController.this.currentAspectRatioFrameLayoutRatio = i == 0 ? 1.0f : (i2 * f) / i;
            if (MediaController.this.currentAspectRatioFrameLayout != null) {
                MediaController.this.currentAspectRatioFrameLayout.setAspectRatio(MediaController.this.currentAspectRatioFrameLayoutRatio, MediaController.this.currentAspectRatioFrameLayoutRotation);
            }
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
            if (MediaController.this.currentAspectRatioFrameLayout != null && !MediaController.this.currentAspectRatioFrameLayout.isDrawingReady()) {
                MediaController.this.isDrawingWasReady = true;
                MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                MediaController.this.currentTextureViewContainer.setTag(1);
            }
            if (MediaController.this.videoPlayer == null || !CastSync.isActive()) {
                return;
            }
            MediaController.this.videoPlayer.setMute(true);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            if (MediaController.this.videoPlayer == null) {
                return false;
            }
            if (MediaController.this.pipSwitchingState == 2) {
                if (MediaController.this.currentAspectRatioFrameLayout != null) {
                    if (MediaController.this.isDrawingWasReady) {
                        MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                    }
                    if (MediaController.this.currentAspectRatioFrameLayout.getParent() == null) {
                        MediaController.this.currentTextureViewContainer.addView(MediaController.this.currentAspectRatioFrameLayout);
                    }
                    if (MediaController.this.currentTextureView.getSurfaceTexture() != surfaceTexture) {
                        MediaController.this.currentTextureView.setSurfaceTexture(surfaceTexture);
                    }
                    MediaController.this.videoPlayer.setTextureView(MediaController.this.currentTextureView);
                }
                MediaController.this.pipSwitchingState = 0;
                return true;
            }
            if (MediaController.this.pipSwitchingState == 1) {
                if (MediaController.this.baseActivity != null) {
                    if (MediaController.this.pipRoundVideoView == null) {
                        try {
                            MediaController.this.pipRoundVideoView = new PipRoundVideoView();
                            MediaController.this.pipRoundVideoView.show(MediaController.this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$9$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$onSurfaceDestroyed$0();
                                }
                            });
                        } catch (Exception unused) {
                            MediaController.this.pipRoundVideoView = null;
                        }
                    }
                    if (MediaController.this.pipRoundVideoView != null) {
                        if (MediaController.this.pipRoundVideoView.getTextureView().getSurfaceTexture() != surfaceTexture) {
                            MediaController.this.pipRoundVideoView.getTextureView().setSurfaceTexture(surfaceTexture);
                        }
                        MediaController.this.videoPlayer.setTextureView(MediaController.this.pipRoundVideoView.getTextureView());
                    }
                }
                MediaController.this.pipSwitchingState = 0;
                return true;
            }
            if (!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isInjectingVideoPlayer()) {
                return false;
            }
            PhotoViewer.getInstance().injectVideoPlayerSurface(surfaceTexture);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceDestroyed$0() {
            MediaController.this.cleanupPlayer(true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playMessage$20() {
        cleanupPlayer(true, true);
    }

    public void syncCastedPlayer() {
        if (this.playingMessageObject == null) {
            return;
        }
        this.ignorePlayerUpdate = true;
        if (CastSync.isActive() && !CastSync.isUpdatePending()) {
            long position = CastSync.getPosition();
            long progressMs = getProgressMs(this.playingMessageObject);
            if (progressMs >= 0 && position >= 0 && Math.abs(progressMs - position) > 1000) {
                seekToProgressMs(this.playingMessageObject, position);
            }
            if (CastSync.isPlaying()) {
                playMessage(this.playingMessageObject);
            } else {
                lambda$startAudioAgain$7(this.playingMessageObject);
            }
            setPlaybackSpeed(true, CastSync.getSpeed());
        }
        setPlayerVolume();
        this.ignorePlayerUpdate = false;
    }

    public long getCurrentPosition() {
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject == null) {
            return -1L;
        }
        return getProgressMs(messageObject);
    }

    /* JADX WARN: Code duplicated, block: B:108:0x01a2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
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
    public ChromecastMediaVariations getCurrentChromecastMedia() throws Throwable {
        FileOutputStream fileOutputStream;
        MessageObject messageObject = this.playingMessageObject;
        FileOutputStream fileOutputStream2 = null;
        file = null;
        file = null;
        File file = null;
        if (messageObject == null) {
            return null;
        }
        String musicTitle = messageObject.getMusicTitle();
        String musicAuthor = this.playingMessageObject.getMusicAuthor();
        TLRPC.Document document = this.playingMessageObject.getDocument();
        if (this.playingMessageObject.isRoundVideo() || this.playingMessageObject.isVideo() || this.playingMessageObject.isMusic()) {
            MessageObject messageObject2 = this.playingMessageObject;
            File file2 = (!messageObject2.attachPathExists || messageObject2.messageOwner == null) ? null : new File(this.playingMessageObject.messageOwner.attachPath);
            if (file2 == null || !file2.exists()) {
                file2 = FileLoader.getInstance(this.playingMessageObject.currentAccount).getPathToMessage(this.playingMessageObject.messageOwner);
            }
            if (file2 != null && file2.exists()) {
                String mimeType = this.playingMessageObject.getMimeType();
                Uri uri = Uri.parse("file://" + file2.getAbsolutePath());
                MediaMetadata mediaMetadata = new MediaMetadata();
                AudioInfo audioInfo = this.audioInfo;
                if (audioInfo != null) {
                    if (!TextUtils.isEmpty(audioInfo.getTitle())) {
                        mediaMetadata.putString("com.google.android.gms.cast.metadata.TITLE", this.audioInfo.getTitle());
                    }
                    if (!TextUtils.isEmpty(this.audioInfo.getArtist())) {
                        mediaMetadata.putString("com.google.android.gms.cast.metadata.ARTIST", this.audioInfo.getArtist());
                    }
                    if (!TextUtils.isEmpty(this.audioInfo.getAlbum())) {
                        mediaMetadata.putString("com.google.android.gms.cast.metadata.ALBUM_TITLE", this.audioInfo.getAlbum());
                    }
                    if (!TextUtils.isEmpty(this.audioInfo.getAlbumArtist())) {
                        mediaMetadata.putString("com.google.android.gms.cast.metadata.ALBUM_ARTIST", this.audioInfo.getAlbumArtist());
                    }
                    if (!TextUtils.isEmpty(this.audioInfo.getComposer())) {
                        mediaMetadata.putString("com.google.android.gms.cast.metadata.COMPOSER", this.audioInfo.getComposer());
                    }
                    if (this.audioInfo.getDisc() != 0) {
                        mediaMetadata.putInt("com.google.android.gms.cast.metadata.DISC_NUMBER", this.audioInfo.getDisc());
                    }
                    if (this.audioInfo.getTrack() != 0) {
                        mediaMetadata.putInt("com.google.android.gms.cast.metadata.TRACK_NUMBER", this.audioInfo.getTrack());
                    }
                    if (this.audioInfo.getCover() != null) {
                        File coverFile = this.audioInfo.getCoverFile();
                        if (coverFile == null || !coverFile.exists()) {
                            File fileMakeCacheFile = StoryEntry.makeCacheFile(UserConfig.selectedAccount, "jpg");
                            try {
                                Bitmap cover = this.audioInfo.getCover();
                                Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
                                fileOutputStream = new FileOutputStream(fileMakeCacheFile);
                                try {
                                    try {
                                        cover.compress(compressFormat, 80, fileOutputStream);
                                        try {
                                            fileOutputStream.close();
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                        file = fileMakeCacheFile;
                                    } catch (Exception e2) {
                                        e = e2;
                                        FileLog.e(e);
                                        if (fileOutputStream != null) {
                                            try {
                                                fileOutputStream.close();
                                            } catch (Exception e3) {
                                                FileLog.e(e3);
                                            }
                                        }
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    fileOutputStream2 = fileOutputStream;
                                    if (fileOutputStream2 != null) {
                                        try {
                                            fileOutputStream2.close();
                                        } catch (Exception e4) {
                                            FileLog.e(e4);
                                        }
                                    }
                                    throw th;
                                }
                            } catch (Exception e5) {
                                e = e5;
                                fileOutputStream = null;
                            } catch (Throwable th2) {
                                th = th2;
                                if (fileOutputStream2 != null) {
                                    fileOutputStream2.close();
                                }
                                throw th;
                            }
                            this.audioInfo.setCoverFile(file);
                            coverFile = file;
                        }
                        if (coverFile != null && coverFile.exists()) {
                            mediaMetadata.addImage(new WebImage(Uri.parse(ChromecastFileServer.getUrlToSource(ChromecastFileServer.getHost(), ChromecastController.getInstance().setCover(coverFile)))));
                        }
                    }
                }
                return ChromecastMediaVariations.of(ChromecastMedia.Builder.fromUri(uri, "/player_" + this.playingMessageObject.getId(), mimeType).setTitle(musicTitle).setSubtitle(musicAuthor).setMetadata(mediaMetadata).build());
            }
        }
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(document != null ? document.id : this.playingMessageObject.getId());
            sb.append(_UrlKt.FRAGMENT_ENCODE_SET);
            return videoPlayer.getCurrentChromecastMedia(sb.toString(), musicTitle, musicAuthor);
        }
        VideoPlayer videoPlayer2 = this.audioPlayer;
        if (videoPlayer2 == null) {
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(document != null ? document.id : this.playingMessageObject.getId());
        sb2.append(_UrlKt.FRAGMENT_ENCODE_SET);
        return videoPlayer2.getCurrentChromecastMedia(sb2.toString(), musicTitle, musicAuthor);
    }

    private boolean canStartMusicPlayerService() {
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject != null) {
            return ((!messageObject.isMusic() && !this.playingMessageObject.isVoice() && !this.playingMessageObject.isRoundVideo()) || this.playingMessageObject.isVoiceOnce() || this.playingMessageObject.isRoundOnce()) ? false : true;
        }
        return false;
    }

    public void updateSilent(boolean z) {
        this.isSilent = z;
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.setLooping(z);
        }
        setPlayerVolume();
        checkVolumeBarUI();
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject != null) {
            NotificationCenter notificationCenter = NotificationCenter.getInstance(messageObject.currentAccount);
            int i = NotificationCenter.messagePlayingPlayStateChanged;
            MessageObject messageObject2 = this.playingMessageObject;
            notificationCenter.lambda$postNotificationNameOnUIThread$1(i, Integer.valueOf(messageObject2 != null ? messageObject2.getId() : 0));
        }
    }

    public AudioInfo getAudioInfo() {
        return this.audioInfo;
    }

    public void setPlaybackOrderType(int i) {
        boolean z = SharedConfig.shuffleMusic;
        SharedConfig.setPlaybackOrderType(i);
        boolean z2 = SharedConfig.shuffleMusic;
        if (z != z2) {
            if (z2) {
                buildShuffledPlayList();
                return;
            }
            MessageObject messageObject = this.playingMessageObject;
            if (messageObject != null) {
                int iIndexOf = this.playlist.indexOf(messageObject);
                this.currentPlaylistNum = iIndexOf;
                if (iIndexOf == -1) {
                    clearPlaylist();
                    cleanupPlayer(true, true);
                }
            }
        }
    }

    public boolean isStreamingCurrentAudio() {
        return this.isStreamingCurrentAudio;
    }

    public boolean isCurrentPlayer(VideoPlayer videoPlayer) {
        return this.videoPlayer == videoPlayer || this.audioPlayer == videoPlayer;
    }

    public void tryResumePausedAudio() {
        MessageObject playingMessageObject = getPlayingMessageObject();
        if (playingMessageObject != null && isMessagePaused() && this.wasPlayingAudioBeforePause && (playingMessageObject.isVoice() || playingMessageObject.isMusic())) {
            playMessage(playingMessageObject);
        }
        this.wasPlayingAudioBeforePause = false;
    }

    /* JADX INFO: renamed from: pauseMessage, reason: merged with bridge method [inline-methods] */
    public boolean lambda$startAudioAgain$7(MessageObject messageObject) {
        return pauseMessage(messageObject, true);
    }

    public boolean pauseMessage(MessageObject messageObject, boolean z) {
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && this.playingMessageObject != null && isSamePlayingMessage(messageObject)) {
            stopProgressTimer();
            try {
                if (this.audioPlayer != null) {
                    if (z && !CastSync.isActive() && !this.playingMessageObject.isVoice() && this.playingMessageObject.getDuration() * ((double) (1.0f - this.playingMessageObject.audioProgress)) > 1.0d && LaunchActivity.isResumed) {
                        ValueAnimator valueAnimator = this.audioVolumeAnimator;
                        if (valueAnimator != null) {
                            valueAnimator.removeAllUpdateListeners();
                            this.audioVolumeAnimator.cancel();
                        }
                        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                        this.audioVolumeAnimator = valueAnimatorOfFloat;
                        valueAnimatorOfFloat.addUpdateListener(this.audioVolumeUpdateListener);
                        this.audioVolumeAnimator.setDuration(300L);
                        this.audioVolumeAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.messenger.MediaController.12
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (MediaController.this.audioPlayer != null) {
                                    MediaController.this.audioPlayer.pause();
                                }
                            }
                        });
                        this.audioVolumeAnimator.start();
                    } else {
                        this.audioPlayer.pause();
                    }
                } else {
                    VideoPlayer videoPlayer = this.videoPlayer;
                    if (videoPlayer != null) {
                        videoPlayer.pause();
                    }
                }
                checkAudioFocus(messageObject, false);
                this.isPaused = true;
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
                try {
                    CastSync.check(1);
                    if (!this.ignorePlayerUpdate) {
                        if (ChromecastController.getInstance().isCasting()) {
                            ChromecastController.getInstance().setCurrentMediaAndCastIfNeeded(getCurrentChromecastMedia());
                        }
                        CastSync.setPlaying(false);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                return true;
            } catch (Exception e2) {
                FileLog.e(e2);
                this.isPaused = false;
            }
        }
        return false;
    }

    private boolean resumeAudio(MessageObject messageObject) {
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && this.playingMessageObject != null && isSamePlayingMessage(messageObject)) {
            try {
                startProgressTimer(this.playingMessageObject);
                ValueAnimator valueAnimator = this.audioVolumeAnimator;
                if (valueAnimator != null) {
                    valueAnimator.removeAllListeners();
                    this.audioVolumeAnimator.cancel();
                }
                if (!messageObject.isVoice() && !messageObject.isRoundVideo()) {
                    ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.audioVolume, 1.0f);
                    this.audioVolumeAnimator = valueAnimatorOfFloat;
                    valueAnimatorOfFloat.addUpdateListener(this.audioVolumeUpdateListener);
                    this.audioVolumeAnimator.setDuration(300L);
                    this.audioVolumeAnimator.start();
                } else {
                    this.audioVolume = 1.0f;
                    setPlayerVolume();
                }
                VideoPlayer videoPlayer = this.audioPlayer;
                if (videoPlayer != null) {
                    videoPlayer.play();
                } else {
                    VideoPlayer videoPlayer2 = this.videoPlayer;
                    if (videoPlayer2 != null) {
                        videoPlayer2.play();
                    }
                }
                checkAudioFocus(messageObject, true);
                this.isPaused = false;
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
                try {
                    CastSync.check(1);
                    if (!this.ignorePlayerUpdate) {
                        CastSync.setPlaying(true);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                return true;
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        return false;
    }

    public boolean isVideoDrawingReady() {
        AspectRatioFrameLayout aspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        return aspectRatioFrameLayout != null && aspectRatioFrameLayout.isDrawingReady();
    }

    public ArrayList<MessageObject> getPlaylist() {
        return this.playlist;
    }

    public MessagesController.SavedMusicList getMusicList() {
        return this.currentSavedMusicList;
    }

    public boolean isPlayingMessage(MessageObject messageObject) {
        MessageObject messageObject2;
        if (messageObject != null && messageObject.isRepostPreview) {
            return false;
        }
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && (messageObject2 = this.playingMessageObject) != null) {
            long j = messageObject2.eventId;
            if ((j != 0 && j == messageObject.eventId) || isSamePlayingMessage(messageObject)) {
                boolean z = this.downloadingCurrentMessage;
                return !z;
            }
        }
        return false;
    }

    public boolean isPlayingMessageAndReadyToDraw(MessageObject messageObject) {
        return this.isDrawingWasReady && isPlayingMessage(messageObject);
    }

    public boolean isMessagePaused() {
        return this.isPaused || this.downloadingCurrentMessage;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isDownloadingCurrentMessage() {
        return this.downloadingCurrentMessage;
    }

    public void setReplyingMessage(MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem) {
        this.recordReplyingMsg = messageObject;
        this.recordReplyingTopMsg = messageObject2;
        this.recordReplyingStory = storyItem;
    }

    public void requestRecordAudioFocus(boolean z) {
        if (z) {
            if (!this.hasRecordAudioFocus && SharedConfig.pauseMusicOnRecord && NotificationsController.audioManager.requestAudioFocus(this.audioRecordFocusChangedListener, 3, 2) == 1) {
                this.hasRecordAudioFocus = true;
                return;
            }
            return;
        }
        if (this.hasRecordAudioFocus) {
            NotificationsController.audioManager.abandonAudioFocus(this.audioRecordFocusChangedListener);
            this.hasRecordAudioFocus = false;
        }
    }

    public void prepareResumedRecording(final int i, final MediaDataController.DraftVoice draftVoice, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final int i2, final String str, final int i3, final long j2, final MessageSuggestionParams messageSuggestionParams) {
        this.manualRecording = false;
        requestRecordAudioFocus(true);
        this.recordQueue.cancelRunnable(this.recordStartRunnable);
        this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$prepareResumedRecording$25(i2, draftVoice, i, j, j2, messageSuggestionParams, messageObject2, messageObject, storyItem, str, i3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareResumedRecording$25(int i, final MediaDataController.DraftVoice draftVoice, final int i2, final long j, long j2, MessageSuggestionParams messageSuggestionParams, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, String str, int i3) {
        setBluetoothScoOn(true);
        this.sendAfterDone = 0;
        TLRPC.TL_document tL_document = new TLRPC.TL_document();
        this.recordingAudio = tL_document;
        this.recordingGuid = i;
        tL_document.dc_id = Integer.MIN_VALUE;
        tL_document.id = draftVoice.id;
        tL_document.user_id = UserConfig.getInstance(i2).getClientUserId();
        TLRPC.TL_document tL_document2 = this.recordingAudio;
        tL_document2.mime_type = "audio/ogg";
        tL_document2.file_reference = new byte[0];
        SharedConfig.saveConfig();
        this.recordingAudioFile = new File(draftVoice.path) { // from class: org.telegram.messenger.MediaController.13
            @Override // java.io.File
            public boolean delete() {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("delete voice file");
                }
                return super.delete();
            }
        };
        FileLoader.getDirectory(4).mkdirs();
        AutoDeleteMediaTask.lockFile(this.recordingAudioFile);
        try {
            this.audioRecorderPaused = true;
            this.recordTimeCount = draftVoice.recordTimeCount;
            this.writtenFrame = draftVoice.writedFrame;
            this.samplesCount = draftVoice.samplesCount;
            this.recordSamples = draftVoice.recordSamples;
            this.recordDialogId = j;
            this.recordMonoForumPeerId = j2;
            this.recordMonoForumSuggestionParams = messageSuggestionParams;
            this.recordTopicId = messageObject == null ? 0L : MessageObject.getTopicId(this.recordingCurrentAccount, messageObject.messageOwner, false);
            this.recordingCurrentAccount = i2;
            this.recordReplyingMsg = messageObject2;
            this.recordReplyingTopMsg = messageObject;
            this.recordReplyingStory = storyItem;
            this.recordQuickReplyShortcut = str;
            this.recordQuickReplyShortcutId = i3;
            final TLRPC.TL_document tL_document3 = this.recordingAudio;
            final File file = this.recordingAudioFile;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$prepareResumedRecording$24(file, tL_document3, draftVoice);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
            this.recordingAudio = null;
            AutoDeleteMediaTask.unlockFile(this.recordingAudioFile);
            this.recordingAudioFile.delete();
            this.recordingAudioFile = null;
            try {
                this.audioRecorder.release();
                this.audioRecorder = null;
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            setBluetoothScoOn(false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$prepareResumedRecording$23(i2, j);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareResumedRecording$23(int i, long j) {
        MediaDataController.getInstance(i).pushDraftVoiceMessage(j, this.recordTopicId, null);
        this.recordStartRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareResumedRecording$24(File file, TLRPC.TL_document tL_document, MediaDataController.DraftVoice draftVoice) {
        if (!file.exists() && BuildVars.DEBUG_VERSION) {
            FileLog.e(new RuntimeException("file not found :( recordTimeCount " + this.recordTimeCount + " writedFrames" + this.writtenFrame));
        }
        tL_document.date = ConnectionsManager.getInstance(this.recordingCurrentAccount).getCurrentTime();
        tL_document.size = (int) file.length();
        TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
        tL_documentAttributeAudio.voice = true;
        short[] sArr = this.recordSamples;
        byte[] waveform2 = getWaveform2(sArr, sArr.length);
        tL_documentAttributeAudio.waveform = waveform2;
        if (waveform2 != null) {
            tL_documentAttributeAudio.flags |= 4;
        }
        tL_documentAttributeAudio.duration = this.recordTimeCount / 1000.0d;
        tL_document.attributes.clear();
        tL_document.attributes.add(tL_documentAttributeAudio);
        NotificationCenter.getInstance(this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordPaused, new Object[0]);
        NotificationCenter.getInstance(this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.audioDidSent, Integer.valueOf(this.recordingGuid), tL_document, file.getAbsolutePath(), Boolean.TRUE, Float.valueOf(draftVoice.left), Float.valueOf(draftVoice.right));
    }

    public boolean isRecordingPaused() {
        return this.audioRecorderPaused;
    }

    private File joinRecord() {
        return joinRecord(this.recordingPrevAudioFile, this.recordingAudioFile, this.recordingAudio);
    }

    private File joinRecord(File file, File file2, TLRPC.TL_document tL_document) {
        if (file != null && file2 != null) {
            File file3 = new File(FileLoader.getDirectory(1), System.currentTimeMillis() + "_" + FileLoader.getAttachFileName(tL_document)) { // from class: org.telegram.messenger.MediaController.14
                @Override // java.io.File
                public boolean delete() {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("delete voice file (joined)");
                    }
                    return super.delete();
                }
            };
            if (joinOpusFiles(file.getAbsolutePath(), file2.getAbsolutePath(), file3.getAbsolutePath())) {
                file2.delete();
                if (file2 == this.recordingAudioFile) {
                    this.recordingAudioFile = file3;
                }
                file2 = file3;
            }
            file.delete();
            if (file == this.recordingPrevAudioFile) {
                this.recordingPrevAudioFile = null;
            }
        }
        return file2;
    }

    public void trimCurrentRecording(final long j, final long j2, final Runnable runnable) {
        if (this.recordingAudioFile == null) {
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
                return;
            }
            return;
        }
        final File file = new File(FileLoader.getDirectory(1), System.currentTimeMillis() + "_" + FileLoader.getAttachFileName(this.recordingAudio)) { // from class: org.telegram.messenger.MediaController.15
            @Override // java.io.File
            public boolean delete() {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("delete voice file (trimmed)");
                }
                return super.delete();
            }
        };
        this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$trimCurrentRecording$26(file, j, j2, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$trimCurrentRecording$26(File file, long j, long j2, Runnable runnable) {
        if (cropOpusFile(this.recordingAudioFile.getAbsolutePath(), file.getAbsolutePath(), j, j2)) {
            File file2 = this.recordingAudioFile;
            if (file2 != null) {
                file2.delete();
            }
            this.recordingAudioFile = file;
            this.recordTimeCount = j2 - j;
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
            }
        }
    }

    public void toggleRecordingPause(final boolean z) {
        this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$toggleRecordingPause$32(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleRecordingPause$32(final boolean z) {
        if (this.recordingAudio == null || this.recordingAudioFile == null) {
            return;
        }
        boolean z2 = this.audioRecorderPaused;
        this.audioRecorderPaused = !z2;
        if (!z2) {
            AudioRecord audioRecord = this.audioRecorder;
            if (audioRecord == null) {
                return;
            }
            this.sendAfterDone = 4;
            audioRecord.stop();
            this.audioRecorder.release();
            this.audioRecorder = null;
            this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$toggleRecordingPause$28(z);
                }
            });
            return;
        }
        this.recordQueue.cancelRunnable(this.recordRunnable);
        this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$toggleRecordingPause$31();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleRecordingPause$28(final boolean z) {
        stopRecord();
        final TLRPC.TL_document tL_document = this.recordingAudio;
        final File fileJoinRecord = joinRecord(this.recordingPrevAudioFile, this.recordingAudioFile, tL_document);
        if (tL_document == null || fileJoinRecord == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$toggleRecordingPause$27(fileJoinRecord, z, tL_document);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleRecordingPause$27(File file, boolean z, TLRPC.TL_document tL_document) {
        boolean zExists = file.exists();
        if (!zExists && BuildVars.DEBUG_VERSION) {
            FileLog.e(new RuntimeException("file not found :( recordTimeCount " + this.recordTimeCount + " writedFrames" + this.writtenFrame));
        }
        if (zExists) {
            MediaDataController.getInstance(this.recordingCurrentAccount).pushDraftVoiceMessage(this.recordDialogId, this.recordTopicId, MediaDataController.DraftVoice.of(this, file.getAbsolutePath(), z, 0.0f, 1.0f));
        }
        tL_document.date = ConnectionsManager.getInstance(this.recordingCurrentAccount).getCurrentTime();
        tL_document.size = (int) file.length();
        TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
        tL_documentAttributeAudio.voice = true;
        byte[] waveform = getWaveform(file.getAbsolutePath());
        tL_documentAttributeAudio.waveform = waveform;
        if (waveform != null) {
            tL_documentAttributeAudio.flags |= 4;
        }
        tL_documentAttributeAudio.duration = this.recordTimeCount / 1000.0d;
        tL_document.attributes.clear();
        tL_document.attributes.add(tL_documentAttributeAudio);
        NotificationCenter.getInstance(this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordPaused, new Object[0]);
        NotificationCenter.getInstance(this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.audioDidSent, Integer.valueOf(this.recordingGuid), tL_document, file.getAbsolutePath());
        requestRecordAudioFocus(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleRecordingPause$31() {
        this.recordingPrevAudioFile = this.recordingAudioFile;
        File file = new File(FileLoader.getDirectory(1), System.currentTimeMillis() + "_" + FileLoader.getAttachFileName(this.recordingAudio)) { // from class: org.telegram.messenger.MediaController.16
            @Override // java.io.File
            public boolean delete() {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("delete voice file (from resume)");
                }
                return super.delete();
            }
        };
        this.recordingAudioFile = file;
        if (startRecord(file.getPath(), this.sampleRate) == 0) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$toggleRecordingPause$29();
                }
            });
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("cant resume audio encoder");
                return;
            }
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$toggleRecordingPause$30();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleRecordingPause$29() {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStartError, Integer.valueOf(this.recordingGuid));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleRecordingPause$30() {
        requestRecordAudioFocus(true);
        this.audioRecorder = new AudioRecord(0, this.sampleRate, 16, 2, this.recordBufferSize);
        this.recordStartTime = System.currentTimeMillis();
        this.writtenFrame = 0;
        this.samplesCount = 0L;
        this.fileBuffer.rewind();
        this.audioRecorder.startRecording();
        this.recordQueue.postRunnable(this.recordRunnable);
        NotificationCenter.getInstance(this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordResumed, new Object[0]);
    }

    public void startRecording(final int i, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final int i2, boolean z, final String str, final int i3, final long j2, final MessageSuggestionParams messageSuggestionParams) {
        MessageObject messageObject3 = this.playingMessageObject;
        boolean z2 = (messageObject3 == null || !isPlayingMessage(messageObject3) || isMessagePaused()) ? false : true;
        this.manualRecording = z;
        requestRecordAudioFocus(true);
        try {
            this.feedbackView.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        DispatchQueue dispatchQueue = this.recordQueue;
        Runnable runnable = new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startRecording$37(i, i2, j, j2, messageSuggestionParams, messageObject2, messageObject, storyItem, str, i3);
            }
        };
        this.recordStartRunnable = runnable;
        dispatchQueue.postRunnable(runnable, z2 ? 500L : 50L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$37(final int i, final int i2, long j, long j2, MessageSuggestionParams messageSuggestionParams, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, String str, int i3) {
        if (this.audioRecorder != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startRecording$33(i, i2);
                }
            });
            return;
        }
        setBluetoothScoOn(true);
        this.sendAfterDone = 0;
        TLRPC.TL_document tL_document = new TLRPC.TL_document();
        this.recordingAudio = tL_document;
        this.recordingGuid = i2;
        tL_document.file_reference = new byte[0];
        tL_document.dc_id = Integer.MIN_VALUE;
        tL_document.id = SharedConfig.getLastLocalId();
        this.recordingAudio.user_id = UserConfig.getInstance(i).getClientUserId();
        TLRPC.TL_document tL_document2 = this.recordingAudio;
        tL_document2.mime_type = "audio/ogg";
        tL_document2.file_reference = new byte[0];
        SharedConfig.saveConfig();
        this.recordingAudioFile = new File(FileLoader.getDirectory(1), System.currentTimeMillis() + "_" + FileLoader.getAttachFileName(this.recordingAudio)) { // from class: org.telegram.messenger.MediaController.17
            @Override // java.io.File
            public boolean delete() {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("delete voice file");
                }
                return super.delete();
            }
        };
        FileLoader.getDirectory(4).mkdirs();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start recording internal " + this.recordingAudioFile.getPath() + " " + this.recordingAudioFile.exists());
        }
        AutoDeleteMediaTask.lockFile(this.recordingAudioFile);
        try {
            if (startRecord(this.recordingAudioFile.getPath(), this.sampleRate) == 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda36
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$startRecording$34(i, i2);
                    }
                });
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("cant init encoder");
                    return;
                }
                return;
            }
            this.audioRecorderPaused = false;
            this.audioRecorder = new AudioRecord(0, this.sampleRate, 16, 2, this.recordBufferSize);
            this.recordStartTime = System.currentTimeMillis();
            long topicId = 0;
            this.recordTimeCount = 0L;
            this.writtenFrame = 0;
            this.samplesCount = 0L;
            this.recordDialogId = j;
            this.recordMonoForumPeerId = j2;
            this.recordMonoForumSuggestionParams = messageSuggestionParams;
            if (messageObject != null) {
                topicId = MessageObject.getTopicId(this.recordingCurrentAccount, messageObject.messageOwner, false);
            }
            this.recordTopicId = topicId;
            this.recordingCurrentAccount = i;
            this.recordReplyingMsg = messageObject2;
            this.recordReplyingTopMsg = messageObject;
            this.recordReplyingStory = storyItem;
            this.recordQuickReplyShortcut = str;
            this.recordQuickReplyShortcutId = i3;
            this.fileBuffer.rewind();
            this.audioRecorder.startRecording();
            this.recordQueue.postRunnable(this.recordRunnable);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startRecording$36(i, i2);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
            this.recordingAudio = null;
            stopRecord();
            AutoDeleteMediaTask.unlockFile(this.recordingAudioFile);
            this.recordingAudioFile.delete();
            this.recordingAudioFile = null;
            File file = this.recordingPrevAudioFile;
            if (file != null) {
                file.delete();
                this.recordingPrevAudioFile = null;
            }
            try {
                this.audioRecorder.release();
                this.audioRecorder = null;
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            setBluetoothScoOn(false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startRecording$35(i, i2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$33(int i, int i2) {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStartError, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$34(int i, int i2) {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStartError, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$35(int i, int i2) {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStartError, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$36(int i, int i2) {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStarted, Integer.valueOf(i2), Boolean.TRUE);
    }

    public void generateWaveform(final MessageObject messageObject) {
        final String str = messageObject.getId() + "_" + messageObject.getDialogId();
        final String absolutePath = FileLoader.getInstance(messageObject.currentAccount).getPathToMessage(messageObject.messageOwner).getAbsolutePath();
        if (this.generatingWaveform.containsKey(str)) {
            return;
        }
        this.generatingWaveform.put(str, messageObject);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$generateWaveform$39(absolutePath, str, messageObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateWaveform$39(String str, final String str2, final MessageObject messageObject) {
        try {
            final byte[] waveform = getWaveform(str);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$generateWaveform$38(str2, waveform, messageObject);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateWaveform$38(String str, byte[] bArr, MessageObject messageObject) {
        MessageObject messageObjectRemove = this.generatingWaveform.remove(str);
        if (messageObjectRemove == null || bArr == null || messageObjectRemove.getDocument() == null) {
            return;
        }
        for (int i = 0; i < messageObjectRemove.getDocument().attributes.size(); i++) {
            TLRPC.DocumentAttribute documentAttribute = messageObjectRemove.getDocument().attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                documentAttribute.waveform = bArr;
                documentAttribute.flags |= 4;
                break;
            }
        }
        TLRPC.TL_messages_messages tL_messages_messages = new TLRPC.TL_messages_messages();
        tL_messages_messages.messages.add(messageObjectRemove.messageOwner);
        MessagesStorage.getInstance(messageObjectRemove.currentAccount).putMessages((TLRPC.messages_Messages) tL_messages_messages, messageObjectRemove.getDialogId(), -1, 0, false, messageObject.scheduled ? 1 : 0, 0L);
        ArrayList arrayList = new ArrayList();
        arrayList.add(messageObjectRemove);
        NotificationCenter.getInstance(messageObjectRemove.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.replaceMessagesObjects, Long.valueOf(messageObjectRemove.getDialogId()), arrayList);
    }

    public void cleanRecording(boolean z) {
        File file;
        File file2;
        this.recordingAudio = null;
        AutoDeleteMediaTask.unlockFile(this.recordingAudioFile);
        if (z && (file2 = this.recordingAudioFile) != null) {
            try {
                file2.delete();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        this.recordingAudioFile = null;
        if (z && (file = this.recordingPrevAudioFile) != null) {
            file.delete();
        }
        this.recordingPrevAudioFile = null;
        this.manualRecording = false;
        this.raiseToEarRecord = false;
        this.ignoreOnPause = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRecordingInternal(final int i, final boolean z, final int i2, final boolean z2, final long j) {
        final File file;
        if (i != 0 && (file = this.recordingAudioFile) != null) {
            final TLRPC.TL_document tL_document = this.recordingAudio;
            final File file2 = this.recordingPrevAudioFile;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("stop recording internal filename " + this.recordingAudioFile.getPath());
            }
            this.fileEncodingQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$stopRecordingInternal$41(file2, file, tL_document, i, z, i2, z2, j);
                }
            });
        } else {
            AutoDeleteMediaTask.unlockFile(this.recordingAudioFile);
            File file3 = this.recordingAudioFile;
            if (file3 != null) {
                file3.delete();
            }
            requestRecordAudioFocus(false);
        }
        try {
            AudioRecord audioRecord = this.audioRecorder;
            if (audioRecord != null) {
                audioRecord.release();
                this.audioRecorder = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.recordingAudio = null;
        this.recordingPrevAudioFile = null;
        this.recordingAudioFile = null;
        this.manualRecording = false;
        this.raiseToEarRecord = false;
        this.ignoreOnPause = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRecordingInternal$41(File file, File file2, final TLRPC.TL_document tL_document, final int i, final boolean z, final int i2, final boolean z2, final long j) {
        stopRecord();
        final File fileJoinRecord = joinRecord(file, file2, tL_document);
        if (fileJoinRecord == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("stop recording recordingAudioFileToSend == null in queue");
                return;
            }
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("stop recording internal in queue " + fileJoinRecord.exists() + " " + fileJoinRecord.length());
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$stopRecordingInternal$40(fileJoinRecord, tL_document, i, z, i2, z2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRecordingInternal$40(File file, TLRPC.TL_document tL_document, int i, boolean z, int i2, boolean z2, long j) {
        boolean z3;
        char c;
        String str;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("stop recording internal ");
            if (file == null) {
                str = "null";
            } else {
                str = file.exists() + " " + file.length() + "  recordTimeCount " + this.recordTimeCount + " writedFrames" + this.writtenFrame;
            }
            sb.append(str);
            FileLog.d(sb.toString());
        }
        if ((file == null || !file.exists()) && BuildVars.DEBUG_VERSION) {
            FileLog.e(new RuntimeException("file not found :( recordTimeCount " + this.recordTimeCount + " writedFrames" + this.writtenFrame));
        }
        MediaDataController.getInstance(this.recordingCurrentAccount).pushDraftVoiceMessage(this.recordDialogId, this.recordTopicId, null);
        tL_document.date = ConnectionsManager.getInstance(this.recordingCurrentAccount).getCurrentTime();
        tL_document.size = file == null ? 0L : (int) file.length();
        TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
        tL_documentAttributeAudio.voice = true;
        byte[] waveform = getWaveform(file.getAbsolutePath());
        tL_documentAttributeAudio.waveform = waveform;
        if (waveform != null) {
            tL_documentAttributeAudio.flags |= 4;
        }
        long j2 = this.recordTimeCount;
        tL_documentAttributeAudio.duration = j2 / 1000.0d;
        tL_document.attributes.clear();
        tL_document.attributes.add(tL_documentAttributeAudio);
        if (j2 > 700) {
            if (i == 1) {
                c = 1;
                SendMessagesHelper.SendMessageParams sendMessageParamsOf = SendMessagesHelper.SendMessageParams.of(tL_document, null, file.getAbsolutePath(), this.recordDialogId, this.recordReplyingMsg, this.recordReplyingTopMsg, null, null, null, null, z, i2, 0, z2 ? Integer.MAX_VALUE : 0, null, null, false);
                sendMessageParamsOf.monoForumPeer = this.recordMonoForumPeerId;
                sendMessageParamsOf.suggestionParams = this.recordMonoForumSuggestionParams;
                sendMessageParamsOf.replyToStoryItem = this.recordReplyingStory;
                sendMessageParamsOf.quick_reply_shortcut = this.recordQuickReplyShortcut;
                sendMessageParamsOf.quick_reply_shortcut_id = this.recordQuickReplyShortcutId;
                sendMessageParamsOf.payStars = j;
                SendMessagesHelper.getInstance(this.recordingCurrentAccount).sendMessage(sendMessageParamsOf);
            } else {
                c = 1;
            }
            NotificationCenter notificationCenter = NotificationCenter.getInstance(this.recordingCurrentAccount);
            int i3 = NotificationCenter.audioDidSent;
            Integer numValueOf = Integer.valueOf(this.recordingGuid);
            TLRPC.TL_document tL_document2 = i == 2 ? tL_document : null;
            String absolutePath = i == 2 ? file.getAbsolutePath() : null;
            Object[] objArr = new Object[3];
            z3 = false;
            objArr[0] = numValueOf;
            objArr[c] = tL_document2;
            objArr[2] = absolutePath;
            notificationCenter.lambda$postNotificationNameOnUIThread$1(i3, objArr);
        } else {
            z3 = false;
            NotificationCenter.getInstance(this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.audioRecordTooShort, Integer.valueOf(this.recordingGuid), Boolean.FALSE, Integer.valueOf((int) j2));
            AutoDeleteMediaTask.unlockFile(file);
            file.delete();
        }
        requestRecordAudioFocus(z3);
    }

    public void stopRecording(final int i, final boolean z, final int i2, final boolean z2, final long j) {
        Runnable runnable = this.recordStartRunnable;
        if (runnable != null) {
            this.recordQueue.cancelRunnable(runnable);
            this.recordStartRunnable = null;
        }
        this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$stopRecording$43(i, z, i2, z2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRecording$43(final int i, boolean z, int i2, boolean z2, long j) {
        if (this.sendAfterDone == 3) {
            this.sendAfterDone = 0;
            stopRecordingInternal(i, z, i2, z2, j);
            return;
        }
        AudioRecord audioRecord = this.audioRecorder;
        if (audioRecord == null) {
            this.recordingAudio = null;
            this.manualRecording = false;
            this.raiseToEarRecord = false;
            this.ignoreOnPause = false;
            return;
        }
        try {
            this.sendAfterDone = i;
            this.sendAfterDoneNotify = z;
            this.sendAfterDoneScheduleDate = i2;
            this.sendAfterDoneOnce = z2;
            this.sendAfterDonePayStars = j;
            audioRecord.stop();
            setBluetoothScoOn(false);
        } catch (Exception e) {
            FileLog.e(e);
            if (this.recordingAudioFile != null) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("delete voice file");
                }
                this.recordingAudioFile.delete();
            }
        }
        if (i == 0) {
            stopRecordingInternal(0, false, 0, false, 0L);
        }
        try {
            this.feedbackView.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$stopRecording$42(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRecording$42(int i) {
        NotificationCenter.getInstance(this.recordingCurrentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStopped, Integer.valueOf(this.recordingGuid), Integer.valueOf(i == 2 ? 1 : 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class MediaLoader implements NotificationCenter.NotificationCenterDelegate {
        private boolean cancelled;
        private int copiedFiles;
        private AccountInstance currentAccount;
        private boolean finished;
        private float finishedProgress;
        private boolean isMusic;
        private HashMap<String, MessageObject> loadingMessageObjects = new HashMap<>();
        private ArrayList<MessageObject> messageObjects;
        private MessagesStorage.IntCallback onFinishRunnable;
        private AlertDialog progressDialog;
        private CountDownLatch waitingForFile;

        public MediaLoader(Context context, AccountInstance accountInstance, ArrayList<MessageObject> arrayList, MessagesStorage.IntCallback intCallback) {
            this.currentAccount = accountInstance;
            this.messageObjects = arrayList;
            this.onFinishRunnable = intCallback;
            this.isMusic = arrayList.get(0).isMusic();
            this.currentAccount.getNotificationCenter().addObserver(this, NotificationCenter.fileLoaded);
            this.currentAccount.getNotificationCenter().addObserver(this, NotificationCenter.fileLoadProgressChanged);
            this.currentAccount.getNotificationCenter().addObserver(this, NotificationCenter.fileLoadFailed);
            AlertDialog alertDialog = new AlertDialog(context, 2, PhotoViewer.getInstance().isVisible() ? new DarkThemeResourceProvider() : null);
            this.progressDialog = alertDialog;
            alertDialog.setMessage(LocaleController.getString(R.string.Loading));
            this.progressDialog.setCancelable(true);
            this.progressDialog.setCancelDialog(true);
            this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    this.f$0.lambda$new$0(dialogInterface);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(DialogInterface dialogInterface) {
            this.cancelled = true;
        }

        public void start() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$start$1();
                }
            }, 250L);
            new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() throws Throwable {
                    this.f$0.lambda$start$2();
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$1() {
            if (this.finished) {
                return;
            }
            this.progressDialog.show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:28:0x0082  */
        public /* synthetic */ void lambda$start$2() throws Throwable {
            File externalStoragePublicDirectory;
            File file;
            File pathToAttach;
            try {
                if (Build.VERSION.SDK_INT >= 29) {
                    int size = this.messageObjects.size();
                    for (int i = 0; i < size; i++) {
                        MessageObject messageObject = this.messageObjects.get(i);
                        String string = messageObject.messageOwner.attachPath;
                        TLRPC.Document document = messageObject.getDocument();
                        TLRPC.Document document2 = messageObject.qualityToSave;
                        if (document2 != null) {
                            string = null;
                            document = document2;
                        }
                        String documentFileName = FileLoader.getDocumentFileName(document);
                        if (string != null && string.length() > 0 && !new File(string).exists()) {
                            string = null;
                        }
                        if (TextUtils.isEmpty(string)) {
                            FileLoader fileLoader = FileLoader.getInstance(this.currentAccount.getCurrentAccount());
                            TLRPC.MessageMedia media = MessageObject.getMedia(messageObject);
                            TLRPC.Document document3 = messageObject.qualityToSave;
                            if (document3 != null) {
                                pathToAttach = fileLoader.getPathToAttach(document3, null, false, true);
                            } else {
                                File pathToMessage = fileLoader.getPathToMessage(messageObject.messageOwner, true);
                                if (media instanceof TLRPC.TL_messageMediaDocument) {
                                    TLRPC.TL_messageMediaDocument tL_messageMediaDocument = (TLRPC.TL_messageMediaDocument) media;
                                    if (tL_messageMediaDocument.alt_documents.isEmpty()) {
                                        pathToAttach = pathToMessage;
                                    } else {
                                        pathToAttach = fileLoader.getPathToAttach(tL_messageMediaDocument.alt_documents.get(0), null, false, true);
                                    }
                                } else {
                                    pathToAttach = pathToMessage;
                                }
                            }
                            string = pathToAttach.toString();
                        }
                        File file2 = new File(string);
                        if (!file2.exists()) {
                            this.waitingForFile = new CountDownLatch(1);
                            addMessageToLoad(messageObject);
                            this.waitingForFile.await();
                        }
                        if (this.cancelled) {
                            break;
                        }
                        if (!file2.exists()) {
                            file2 = FileLoader.getInstance(this.currentAccount.getCurrentAccount()).getPathToAttach(messageObject.messageOwner, true);
                            StringBuilder sb = new StringBuilder();
                            sb.append("saving file: correcting path from ");
                            sb.append(string);
                            sb.append(" to ");
                            sb.append(file2 == null ? null : file2.getAbsolutePath());
                            FileLog.d(sb.toString());
                        }
                        if (file2 != null && file2.exists()) {
                            MediaController.saveFileInternal(this.isMusic ? 3 : 2, file2, documentFileName);
                            this.copiedFiles++;
                        }
                    }
                } else {
                    if (this.isMusic) {
                        externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                    } else {
                        externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    }
                    externalStoragePublicDirectory.mkdir();
                    int size2 = this.messageObjects.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        MessageObject messageObject2 = this.messageObjects.get(i2);
                        TLRPC.Document document4 = messageObject2.getDocument();
                        TLRPC.Document document5 = messageObject2.qualityToSave;
                        if (document5 != null) {
                            document4 = document5;
                        }
                        String documentFileName2 = FileLoader.getDocumentFileName(document4);
                        File file3 = new File(externalStoragePublicDirectory, documentFileName2);
                        if (file3.exists()) {
                            int iLastIndexOf = documentFileName2.lastIndexOf(46);
                            int i3 = 0;
                            while (i3 < 10) {
                                File file4 = new File(externalStoragePublicDirectory, iLastIndexOf != -1 ? documentFileName2.substring(0, iLastIndexOf) + "(" + (i3 + 1) + ")" + documentFileName2.substring(iLastIndexOf) : documentFileName2 + "(" + (i3 + 1) + ")");
                                if (!file4.exists()) {
                                    file3 = file4;
                                    break;
                                } else {
                                    i3++;
                                    file3 = file4;
                                }
                            }
                        }
                        if (!file3.exists()) {
                            file3.createNewFile();
                        }
                        String string2 = messageObject2.messageOwner.attachPath;
                        if (messageObject2.qualityToSave != null) {
                            string2 = null;
                        }
                        if (string2 != null && string2.length() > 0 && !new File(string2).exists()) {
                            string2 = null;
                        }
                        if (messageObject2.qualityToSave != null) {
                            file = FileLoader.getInstance(this.currentAccount.getCurrentAccount()).getPathToAttach(messageObject2.qualityToSave, null, false, true);
                        } else {
                            if (string2 == null || string2.length() == 0) {
                                string2 = FileLoader.getInstance(this.currentAccount.getCurrentAccount()).getPathToMessage(messageObject2.messageOwner).toString();
                            }
                            file = new File(string2);
                        }
                        if (!file.exists()) {
                            this.waitingForFile = new CountDownLatch(1);
                            addMessageToLoad(messageObject2);
                            this.waitingForFile.await();
                        }
                        if (file.exists()) {
                            copyFile(file, file3, messageObject2.getMimeType());
                            this.copiedFiles++;
                        }
                    }
                }
                checkIfFinished();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        private void checkIfFinished() {
            if (this.loadingMessageObjects.isEmpty()) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$checkIfFinished$4();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkIfFinished$4() {
            try {
                if (this.progressDialog.isShowing()) {
                    this.progressDialog.dismiss();
                } else {
                    this.finished = true;
                }
                if (this.onFinishRunnable != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$checkIfFinished$3();
                        }
                    });
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.currentAccount.getNotificationCenter().removeObserver(this, NotificationCenter.fileLoaded);
            this.currentAccount.getNotificationCenter().removeObserver(this, NotificationCenter.fileLoadProgressChanged);
            this.currentAccount.getNotificationCenter().removeObserver(this, NotificationCenter.fileLoadFailed);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkIfFinished$3() {
            this.onFinishRunnable.run(this.copiedFiles);
        }

        private void addMessageToLoad(final MessageObject messageObject) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$addMessageToLoad$5(messageObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addMessageToLoad$5(MessageObject messageObject) {
            TLRPC.Document document = messageObject.getDocument();
            TLRPC.Document document2 = messageObject.qualityToSave;
            if (document2 != null) {
                document = document2;
            }
            if (document == null) {
                return;
            }
            this.loadingMessageObjects.put(FileLoader.getAttachFileName(document), messageObject);
            this.currentAccount.getFileLoader().loadFile(document, messageObject, 3, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 0);
        }

        /* JADX WARN: Code duplicated, block: B:119:0x0187 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:144:? A[Catch: all -> 0x0166, SYNTHETIC, TRY_LEAVE, TryCatch #1 {all -> 0x0166, blocks: (B:76:0x015d, B:101:0x018f, B:100:0x018c, B:85:0x016e, B:97:0x0187), top: B:116:0x0015, inners: #3 }] */
        private boolean copyFile(File file, File file2, String str) throws Throwable {
            File file3;
            boolean z;
            FileInputStream fileInputStream;
            Throwable th;
            Throwable th2;
            String str2;
            boolean z2 = false;
            if (AndroidUtilities.isInternalUri(Uri.fromFile(file))) {
                return false;
            }
            try {
                try {
                    FileInputStream fileInputStream2 = new FileInputStream(file);
                    try {
                        try {
                            FileChannel channel = fileInputStream2.getChannel();
                            try {
                                file3 = file2;
                                try {
                                    try {
                                        FileChannel channel2 = new FileOutputStream(file3).getChannel();
                                        try {
                                            long size = channel.size();
                                            try {
                                                if (AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", null).invoke(fileInputStream2.getFD(), null)).intValue())) {
                                                    if (this.progressDialog != null) {
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda1
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                this.f$0.lambda$copyFile$6();
                                                            }
                                                        });
                                                    }
                                                    if (channel2 != null) {
                                                        channel2.close();
                                                    }
                                                    try {
                                                        channel.close();
                                                        try {
                                                            fileInputStream2.close();
                                                            return false;
                                                        } catch (Exception e) {
                                                            e = e;
                                                            z = false;
                                                            FileLog.e(e);
                                                            file3.delete();
                                                            return z;
                                                        }
                                                    } catch (Throwable th3) {
                                                        th = th3;
                                                        z = false;
                                                        fileInputStream = fileInputStream2;
                                                        th = th;
                                                        try {
                                                            fileInputStream.close();
                                                            throw th;
                                                        } catch (Throwable th4) {
                                                            th.addSuppressed(th4);
                                                            throw th;
                                                        }
                                                    }
                                                }
                                                long j = 0;
                                                long j2 = 0;
                                                while (j < size && !this.cancelled) {
                                                    z = z2;
                                                    fileInputStream = fileInputStream2;
                                                    try {
                                                        channel2.transferFrom(channel, j, Math.min(4096L, size - j));
                                                        long j3 = 4096 + j;
                                                        if (j3 >= size || j2 <= SystemClock.elapsedRealtime() - 500) {
                                                            long jElapsedRealtime = SystemClock.elapsedRealtime();
                                                            final int size2 = (int) (this.finishedProgress + (((100.0f / this.messageObjects.size()) * j) / size));
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda2
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    this.f$0.lambda$copyFile$7(size2);
                                                                }
                                                            });
                                                            j2 = jElapsedRealtime;
                                                        }
                                                        j = j3;
                                                        z2 = z;
                                                        fileInputStream2 = fileInputStream;
                                                    } catch (Throwable th5) {
                                                        th = th5;
                                                    }
                                                }
                                                z = z2;
                                                fileInputStream = fileInputStream2;
                                                if (!this.cancelled) {
                                                    if (this.isMusic) {
                                                        AndroidUtilities.addMediaToGallery(file3);
                                                    } else {
                                                        DownloadManager downloadManager = (DownloadManager) ApplicationLoader.applicationContext.getSystemService("download");
                                                        if (TextUtils.isEmpty(str)) {
                                                            MimeTypeMap singleton = MimeTypeMap.getSingleton();
                                                            String name = file3.getName();
                                                            int iLastIndexOf = name.lastIndexOf(46);
                                                            if (iLastIndexOf == -1) {
                                                                str2 = "text/plain";
                                                            } else {
                                                                String mimeTypeFromExtension = singleton.getMimeTypeFromExtension(name.substring(iLastIndexOf + 1).toLowerCase());
                                                                if (TextUtils.isEmpty(mimeTypeFromExtension)) {
                                                                    mimeTypeFromExtension = "text/plain";
                                                                }
                                                                str2 = mimeTypeFromExtension;
                                                            }
                                                        } else {
                                                            str2 = str;
                                                        }
                                                        downloadManager.addCompletedDownload(file3.getName(), file3.getName(), false, str2, file3.getAbsolutePath(), file3.length(), true);
                                                    }
                                                    float size3 = this.finishedProgress + (100.0f / this.messageObjects.size());
                                                    this.finishedProgress = size3;
                                                    final int i = (int) size3;
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda3
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            this.f$0.lambda$copyFile$8(i);
                                                        }
                                                    });
                                                    if (channel2 != null) {
                                                        channel2.close();
                                                    }
                                                    channel.close();
                                                    fileInputStream.close();
                                                    return true;
                                                }
                                                if (channel2 != null) {
                                                    channel2.close();
                                                }
                                                channel.close();
                                                fileInputStream.close();
                                                file3.delete();
                                                return z;
                                            } catch (Throwable th6) {
                                                FileLog.e(th6);
                                            }
                                        } catch (Throwable th7) {
                                            th = th7;
                                            z = z2;
                                            fileInputStream = fileInputStream2;
                                        }
                                        th = th7;
                                        z = z2;
                                        fileInputStream = fileInputStream2;
                                        Throwable th8 = th;
                                        if (channel2 == null) {
                                            throw th8;
                                        }
                                        try {
                                            channel2.close();
                                            throw th8;
                                        } catch (Throwable th9) {
                                            th8.addSuppressed(th9);
                                            throw th8;
                                        }
                                    } catch (Throwable th10) {
                                        th = th10;
                                        th2 = th;
                                        if (channel != null) {
                                            throw th2;
                                        }
                                        try {
                                            channel.close();
                                            throw th2;
                                        } catch (Throwable th11) {
                                            th2.addSuppressed(th11);
                                            throw th2;
                                        }
                                    }
                                } catch (Throwable th12) {
                                    th = th12;
                                    z = false;
                                    fileInputStream = fileInputStream2;
                                    th2 = th;
                                    if (channel != null) {
                                        throw th2;
                                    }
                                    channel.close();
                                    throw th2;
                                }
                            } catch (Throwable th13) {
                                th = th13;
                                file3 = file2;
                            }
                        } catch (Throwable th14) {
                            th = th14;
                            th = th;
                            fileInputStream.close();
                            throw th;
                        }
                    } catch (Throwable th15) {
                        th = th15;
                        file3 = file2;
                    }
                } catch (Exception e2) {
                    e = e2;
                    file3 = file2;
                }
            } catch (Exception e3) {
                e = e3;
                FileLog.e(e);
                file3.delete();
                return z;
            }
            file3.delete();
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$copyFile$6() {
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$copyFile$7(int i) {
            try {
                this.progressDialog.setProgress(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$copyFile$8(int i) {
            try {
                this.progressDialog.setProgress(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i == NotificationCenter.fileLoaded || i == NotificationCenter.fileLoadFailed) {
                if (this.loadingMessageObjects.remove((String) objArr[0]) != null) {
                    this.waitingForFile.countDown();
                    return;
                }
                return;
            }
            if (i == NotificationCenter.fileLoadProgressChanged) {
                if (this.loadingMessageObjects.containsKey((String) objArr[0])) {
                    final int iLongValue = (int) (this.finishedProgress + (((((Long) objArr[1]).longValue() / ((Long) objArr[2]).longValue()) / this.messageObjects.size()) * 100.0f));
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$didReceivedNotification$9(iLongValue);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$9(int i) {
            try {
                this.progressDialog.setProgress(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static void saveFilesFromMessages(Context context, AccountInstance accountInstance, ArrayList<MessageObject> arrayList, MessagesStorage.IntCallback intCallback) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        new MediaLoader(context, accountInstance, arrayList, intCallback).start();
    }

    public static void saveFile(String str, Context context, int i, String str2, String str3) {
        saveFile(str, context, i, str2, str3, null);
    }

    public static void saveFile(String str, Context context, int i, String str2, String str3, Utilities.Callback<Uri> callback) {
        saveFile(str, context, i, str2, str3, callback, true);
    }

    /* JADX WARN: Code duplicated, block: B:13:0x0025  */
    public static void saveFile(String str, Context context, final int i, final String str2, final String str3, final Utilities.Callback<Uri> callback, boolean z) {
        final File file;
        final AlertDialog alertDialog;
        if (str == null || context == null) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            file = null;
        } else {
            File file2 = new File(str);
            if (!file2.exists() || AndroidUtilities.isInternalUri(Uri.fromFile(file2))) {
                file = null;
            } else {
                file = file2;
            }
        }
        if (file == null) {
            return;
        }
        final boolean[] zArr = {false};
        if (file.exists()) {
            final boolean[] zArr2 = new boolean[1];
            if (i != 0) {
                try {
                    final AlertDialog alertDialog2 = new AlertDialog(context, 2);
                    alertDialog2.setMessage(LocaleController.getString(R.string.Loading));
                    alertDialog2.setCanceledOnTouchOutside(false);
                    alertDialog2.setCancelable(true);
                    alertDialog2.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda8
                        @Override // android.content.DialogInterface.OnCancelListener
                        public final void onCancel(DialogInterface dialogInterface) {
                            MediaController.$r8$lambda$OQsxk9XIl050Oj7GW5MwSMV4U0M(zArr, dialogInterface);
                        }
                    });
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.m3135$r8$lambda$Iw__r2i1TFcSX1J2MWUisWw0cI(zArr2, alertDialog2);
                        }
                    }, 250L);
                    alertDialog = alertDialog2;
                } catch (Exception e) {
                    FileLog.e(e);
                    alertDialog = null;
                }
            } else {
                alertDialog = null;
            }
            new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() throws Throwable {
                    MediaController.m3145$r8$lambda$fBsru5e_YyNhMhNroeamuu0pqA(i, file, str2, alertDialog, zArr, str3, callback, zArr2);
                }
            }).start();
        }
    }

    public static /* synthetic */ void $r8$lambda$OQsxk9XIl050Oj7GW5MwSMV4U0M(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    /* JADX INFO: renamed from: $r8$lambda$Iw__r2i1TFcSX1J2MWUisW-w0cI, reason: not valid java name */
    public static /* synthetic */ void m3135$r8$lambda$Iw__r2i1TFcSX1J2MWUisWw0cI(boolean[] zArr, AlertDialog alertDialog) {
        if (zArr[0]) {
            return;
        }
        alertDialog.show();
    }

    /* JADX WARN: Code duplicated, block: B:123:0x01f7 A[Catch: Exception -> 0x001b, TryCatch #9 {Exception -> 0x001b, blocks: (B:3:0x000a, B:5:0x0013, B:134:0x0237, B:12:0x0021, B:32:0x00e2, B:34:0x00e8, B:35:0x00eb, B:120:0x01ee, B:121:0x01f3, B:123:0x01f7, B:128:0x0203, B:129:0x0227, B:130:0x022e, B:14:0x0042, B:16:0x0063, B:18:0x0070, B:20:0x0085, B:26:0x0097, B:28:0x00d1, B:31:0x00de, B:27:0x00ba, B:17:0x006a), top: B:157:0x000a }] */
    /* JADX WARN: Code duplicated, block: B:124:0x01fd  */
    /* JADX WARN: Code duplicated, block: B:126:0x0200  */
    /* JADX WARN: Code duplicated, block: B:128:0x0203 A[Catch: Exception -> 0x001b, TryCatch #9 {Exception -> 0x001b, blocks: (B:3:0x000a, B:5:0x0013, B:134:0x0237, B:12:0x0021, B:32:0x00e2, B:34:0x00e8, B:35:0x00eb, B:120:0x01ee, B:121:0x01f3, B:123:0x01f7, B:128:0x0203, B:129:0x0227, B:130:0x022e, B:14:0x0042, B:16:0x0063, B:18:0x0070, B:20:0x0085, B:26:0x0097, B:28:0x00d1, B:31:0x00de, B:27:0x00ba, B:17:0x006a), top: B:157:0x000a }] */
    /* JADX WARN: Code duplicated, block: B:129:0x0227 A[Catch: Exception -> 0x001b, TryCatch #9 {Exception -> 0x001b, blocks: (B:3:0x000a, B:5:0x0013, B:134:0x0237, B:12:0x0021, B:32:0x00e2, B:34:0x00e8, B:35:0x00eb, B:120:0x01ee, B:121:0x01f3, B:123:0x01f7, B:128:0x0203, B:129:0x0227, B:130:0x022e, B:14:0x0042, B:16:0x0063, B:18:0x0070, B:20:0x0085, B:26:0x0097, B:28:0x00d1, B:31:0x00de, B:27:0x00ba, B:17:0x006a), top: B:157:0x000a }] */
    /* JADX WARN: Code duplicated, block: B:133:0x0235 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:138:0x0245  */
    /* JADX WARN: Code duplicated, block: B:155:0x01d1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:175:? A[Catch: all -> 0x01bb, SYNTHETIC, TRY_LEAVE, TryCatch #14 {all -> 0x01bb, blocks: (B:110:0x01d9, B:109:0x01d6, B:90:0x01b1, B:106:0x01d1), top: B:163:0x00f7, inners: #8 }] */
    /* JADX WARN: Code duplicated, block: B:178:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r25v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r25v1 */
    /* JADX WARN: Type inference failed for: r25v10 */
    /* JADX WARN: Type inference failed for: r25v11 */
    /* JADX WARN: Type inference failed for: r25v12 */
    /* JADX WARN: Type inference failed for: r25v13 */
    /* JADX WARN: Type inference failed for: r25v14 */
    /* JADX WARN: Type inference failed for: r25v15 */
    /* JADX WARN: Type inference failed for: r25v16 */
    /* JADX WARN: Type inference failed for: r25v17 */
    /* JADX WARN: Type inference failed for: r25v18 */
    /* JADX WARN: Type inference failed for: r25v19 */
    /* JADX WARN: Type inference failed for: r25v2 */
    /* JADX WARN: Type inference failed for: r25v20 */
    /* JADX WARN: Type inference failed for: r25v21 */
    /* JADX WARN: Type inference failed for: r25v22 */
    /* JADX WARN: Type inference failed for: r25v3, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r25v4 */
    /* JADX WARN: Type inference failed for: r25v5 */
    /* JADX WARN: Type inference failed for: r25v6 */
    /* JADX WARN: Type inference failed for: r25v7 */
    /* JADX WARN: Type inference failed for: r25v8 */
    /* JADX WARN: Type inference failed for: r25v9 */
    /* JADX WARN: Type inference failed for: r6v11 */
    /* JADX WARN: Type inference failed for: r6v12 */
    /* JADX WARN: Type inference failed for: r6v19, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r6v20, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r6v21 */
    /* JADX WARN: Type inference failed for: r6v22 */
    /* JADX WARN: Type inference failed for: r6v23 */
    /* JADX WARN: Type inference failed for: r6v26, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r6v27 */
    /* JADX WARN: Type inference failed for: r6v28 */
    /* JADX WARN: Type inference failed for: r6v4, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r6v5 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    /* JADX INFO: renamed from: $r8$lambda$fBsru5e_YyNhMhNroeamuu0p-qA, reason: not valid java name */
    public static /* synthetic */ void m3145$r8$lambda$fBsru5e_YyNhMhNroeamuu0pqA(int i, File file, String str, final AlertDialog alertDialog, boolean[] zArr, String str2, final Utilities.Callback callback, final boolean[] zArr2) throws Throwable {
        File externalStoragePublicDirectory;
        ?? file2;
        String str3;
        char c;
        ?? r25;
        char c2;
        final Uri uriFromFile;
        Throwable th;
        Throwable th2;
        try {
            char c3 = 1;
            char c4 = 0;
            if (Build.VERSION.SDK_INT >= 29) {
                uriFromFile = saveFileInternal(i, file, null);
                if (uriFromFile != null) {
                }
                if (c3 != 0 && callback != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda33
                        @Override // java.lang.Runnable
                        public final void run() {
                            callback.run(uriFromFile);
                        }
                    });
                }
                if (alertDialog != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda34
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.m3142$r8$lambda$ZHv9PpmmBv9GE4sgOSGeTdEU60(alertDialog, zArr2);
                        }
                    });
                }
            }
            if (i == 0) {
                File file3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AyuConstants.APP_NAME);
                file3.mkdirs();
                file2 = new File(file3, AndroidUtilities.generateFileName(0, FileLoader.getFileExtension(file)));
            } else if (i == 1) {
                File file4 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), AyuConstants.APP_NAME);
                file4.mkdirs();
                file2 = new File(file4, AndroidUtilities.generateFileName(1, FileLoader.getFileExtension(file)));
            } else {
                if (i == 2) {
                    externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                } else {
                    externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                }
                File file5 = new File(externalStoragePublicDirectory, AyuConstants.APP_NAME);
                file5.mkdirs();
                file2 = new File(file5, (String) str);
                if (file2.exists()) {
                    int iLastIndexOf = str.lastIndexOf(46);
                    int i2 = 0;
                    file2 = file2;
                    while (i2 < 10) {
                        if (iLastIndexOf != -1) {
                            str3 = str.substring(0, iLastIndexOf) + "(" + (i2 + 1) + ")" + str.substring(iLastIndexOf);
                        } else {
                            str3 = ((String) str) + "(" + (i2 + 1) + ")";
                        }
                        File file6 = new File(file5, str3);
                        if (!file6.exists()) {
                            file2 = file6;
                            break;
                        } else {
                            i2++;
                            file2 = file6;
                        }
                    }
                }
            }
            if (!file2.exists()) {
                file2.createNewFile();
            }
            long jCurrentTimeMillis = System.currentTimeMillis() - 500;
            try {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        try {
                            FileChannel channel = fileInputStream.getChannel();
                            try {
                                FileChannel channel2 = new FileOutputStream((File) file2).getChannel();
                                c = 0;
                                try {
                                    try {
                                        long size = channel.size();
                                        try {
                                            if (AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", null).invoke(fileInputStream.getFD(), null)).intValue())) {
                                                if (alertDialog != null) {
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda31
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            MediaController.$r8$lambda$byEPNLpvSxIW0zdApievlt74AmU(alertDialog);
                                                        }
                                                    });
                                                }
                                                if (channel2 != null) {
                                                    try {
                                                        channel2.close();
                                                    } catch (Throwable th3) {
                                                        th2 = th3;
                                                        str = file2;
                                                        if (channel != null) {
                                                            throw th2;
                                                        }
                                                        try {
                                                            channel.close();
                                                            throw th2;
                                                        } catch (Throwable th4) {
                                                            th2.addSuppressed(th4);
                                                            throw th2;
                                                        }
                                                    }
                                                }
                                                try {
                                                    channel.close();
                                                    try {
                                                        fileInputStream.close();
                                                        return;
                                                    } catch (Exception e) {
                                                        e = e;
                                                        str = file2;
                                                        FileLog.e(e);
                                                        c2 = c;
                                                        r25 = str;
                                                        if (zArr[c]) {
                                                            r25.delete();
                                                            c4 = c;
                                                        } else {
                                                            c4 = c2;
                                                        }
                                                        if (c4 != 0) {
                                                            if (i == 2) {
                                                                ((DownloadManager) ApplicationLoader.applicationContext.getSystemService("download")).addCompletedDownload(r25.getName(), r25.getName(), false, str2, r25.getAbsolutePath(), r25.length(), true);
                                                            } else {
                                                                AndroidUtilities.addMediaToGallery(r25.getAbsoluteFile());
                                                            }
                                                        }
                                                        uriFromFile = Uri.fromFile(r25);
                                                        c3 = c4;
                                                        if (c3 != 0) {
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda33
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    callback.run(uriFromFile);
                                                                }
                                                            });
                                                        }
                                                        if (alertDialog != null) {
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda34
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    MediaController.m3142$r8$lambda$ZHv9PpmmBv9GE4sgOSGeTdEU60(alertDialog, zArr2);
                                                                }
                                                            });
                                                        }
                                                    }
                                                } catch (Throwable th5) {
                                                    th = th5;
                                                    str = file2;
                                                    try {
                                                        fileInputStream.close();
                                                        throw th;
                                                    } catch (Throwable th6) {
                                                        th.addSuppressed(th6);
                                                        throw th;
                                                    }
                                                }
                                            }
                                        } catch (Throwable th7) {
                                            FileLog.e(th7);
                                        }
                                        long j = 0;
                                        file2 = file2;
                                        while (j < size && !zArr[0]) {
                                            str = file2;
                                            try {
                                                channel2.transferFrom(channel, j, Math.min(4096L, size - j));
                                                long j2 = j;
                                                if (alertDialog != null && jCurrentTimeMillis <= System.currentTimeMillis() - 500) {
                                                    jCurrentTimeMillis = System.currentTimeMillis();
                                                    final int i3 = (int) ((j2 / size) * 100.0f);
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda32
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            MediaController.$r8$lambda$Q5N9slVmdEuD_WaVRbkRVosrKVc(alertDialog, i3);
                                                        }
                                                    });
                                                }
                                                j = j2 + 4096;
                                                file2 = str;
                                            } catch (Throwable th8) {
                                                th = th8;
                                                Throwable th9 = th;
                                                if (channel2 == null) {
                                                    throw th9;
                                                }
                                                try {
                                                    channel2.close();
                                                    throw th9;
                                                } catch (Throwable th10) {
                                                    th9.addSuppressed(th10);
                                                    throw th9;
                                                }
                                            }
                                        }
                                        r25 = file2;
                                        if (channel2 != null) {
                                            channel2.close();
                                        }
                                        channel.close();
                                        fileInputStream.close();
                                        c2 = 1;
                                        if (zArr[c]) {
                                            r25.delete();
                                            c4 = c;
                                        } else {
                                            c4 = c2;
                                        }
                                        if (c4 != 0) {
                                            if (i == 2) {
                                                ((DownloadManager) ApplicationLoader.applicationContext.getSystemService("download")).addCompletedDownload(r25.getName(), r25.getName(), false, str2, r25.getAbsolutePath(), r25.length(), true);
                                            } else {
                                                AndroidUtilities.addMediaToGallery(r25.getAbsoluteFile());
                                            }
                                        }
                                        uriFromFile = Uri.fromFile(r25);
                                    } catch (Throwable th11) {
                                        th = th11;
                                        str = file2;
                                    }
                                } catch (Throwable th12) {
                                    th = th12;
                                    th2 = th;
                                    str = str;
                                    if (channel != null) {
                                        throw th2;
                                    }
                                    channel.close();
                                    throw th2;
                                }
                            } catch (Throwable th13) {
                                th = th13;
                                str = file2;
                                c = 0;
                            }
                        } catch (Throwable th14) {
                            th = th14;
                            str = file2;
                            c = 0;
                            th = th;
                            str = str;
                            fileInputStream.close();
                            throw th;
                        }
                    } catch (Throwable th15) {
                        th = th15;
                        th = th;
                        str = str;
                        fileInputStream.close();
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    str = file2;
                    c = 0;
                }
            } catch (Exception e3) {
                e = e3;
                FileLog.e(e);
                c2 = c;
                r25 = str;
                if (zArr[c]) {
                    r25.delete();
                    c4 = c;
                } else {
                    c4 = c2;
                }
                if (c4 != 0) {
                    if (i == 2) {
                        ((DownloadManager) ApplicationLoader.applicationContext.getSystemService("download")).addCompletedDownload(r25.getName(), r25.getName(), false, str2, r25.getAbsolutePath(), r25.length(), true);
                    } else {
                        AndroidUtilities.addMediaToGallery(r25.getAbsoluteFile());
                    }
                }
                uriFromFile = Uri.fromFile(r25);
                c3 = c4;
                if (c3 != 0) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda33
                        @Override // java.lang.Runnable
                        public final void run() {
                            callback.run(uriFromFile);
                        }
                    });
                }
                if (alertDialog != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda34
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.m3142$r8$lambda$ZHv9PpmmBv9GE4sgOSGeTdEU60(alertDialog, zArr2);
                        }
                    });
                }
            }
            c3 = c4;
            if (c3 != 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda33
                    @Override // java.lang.Runnable
                    public final void run() {
                        callback.run(uriFromFile);
                    }
                });
            }
        } catch (Exception e4) {
            FileLog.e(e4);
        }
        if (alertDialog != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.m3142$r8$lambda$ZHv9PpmmBv9GE4sgOSGeTdEU60(alertDialog, zArr2);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$byEPNLpvSxIW0zdApievlt74AmU(AlertDialog alertDialog) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static /* synthetic */ void $r8$lambda$Q5N9slVmdEuD_WaVRbkRVosrKVc(AlertDialog alertDialog, int i) {
        try {
            alertDialog.setProgress(i);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$ZHv9PpmmBv9GE4-sgOSGeTdEU60, reason: not valid java name */
    public static /* synthetic */ void m3142$r8$lambda$ZHv9PpmmBv9GE4sgOSGeTdEU60(AlertDialog alertDialog, boolean[] zArr) {
        try {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            } else {
                zArr[0] = true;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Uri saveFileInternal(int i, File file, String str) {
        Uri contentUri;
        try {
            ContentValues contentValues = new ContentValues();
            String fileExtension = FileLoader.getFileExtension(file);
            String mimeTypeFromExtension = fileExtension != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension) : null;
            if ((i == 0 || i == 1) && mimeTypeFromExtension != null) {
                if (mimeTypeFromExtension.startsWith("image")) {
                    i = 0;
                }
                if (mimeTypeFromExtension.startsWith(MediaStreamTrack.VIDEO_TRACK_KIND)) {
                    i = 1;
                }
            }
            if (i == 0) {
                if (str == null) {
                    str = AndroidUtilities.generateFileName(0, fileExtension);
                }
                contentUri = MediaStore.Images.Media.getContentUri("external_primary");
                contentValues.put("relative_path", new File(Environment.DIRECTORY_PICTURES, AyuConstants.APP_NAME) + File.separator);
                contentValues.put("_display_name", str);
                contentValues.put("mime_type", mimeTypeFromExtension);
            } else if (i == 1) {
                if (str == null) {
                    str = AndroidUtilities.generateFileName(1, fileExtension);
                }
                contentValues.put("relative_path", new File(Environment.DIRECTORY_MOVIES, AyuConstants.APP_NAME) + File.separator);
                contentUri = MediaStore.Video.Media.getContentUri("external_primary");
                contentValues.put("_display_name", str);
            } else if (i == 2) {
                if (str == null) {
                    str = file.getName();
                }
                contentValues.put("relative_path", new File(Environment.DIRECTORY_DOWNLOADS, AyuConstants.APP_NAME) + File.separator);
                contentUri = MediaStore.Downloads.getContentUri("external_primary");
                contentValues.put("_display_name", str);
            } else {
                if (str == null) {
                    str = file.getName();
                }
                contentValues.put("relative_path", new File(Environment.DIRECTORY_MUSIC, AyuConstants.APP_NAME) + File.separator);
                contentUri = MediaStore.Audio.Media.getContentUri("external_primary");
                contentValues.put("_display_name", str);
            }
            contentValues.put("mime_type", mimeTypeFromExtension);
            Uri uriInsert = ApplicationLoader.applicationContext.getContentResolver().insert(contentUri, contentValues);
            if (uriInsert != null) {
                FileInputStream fileInputStream = new FileInputStream(file);
                AndroidUtilities.copyFile(fileInputStream, ApplicationLoader.applicationContext.getContentResolver().openOutputStream(uriInsert));
                fileInputStream.close();
            }
            return uriInsert;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:79:0x00c7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static String getStickerExt(Uri uri) throws Throwable {
        InputStream fileInputStream;
        InputStream inputStream = null;
        try {
            fileInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
        } catch (Exception unused) {
            fileInputStream = null;
        } catch (Throwable th) {
            th = th;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            throw th;
        }
        if (fileInputStream == null) {
            try {
                try {
                    File file = new File(uri.getPath());
                    if (file.exists()) {
                        fileInputStream = new FileInputStream(file);
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = fileInputStream;
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        }
        byte[] bArr = new byte[12];
        if (fileInputStream.read(bArr, 0, 12) == 12) {
            byte b = bArr[0];
            if (b == -119 && bArr[1] == 80 && bArr[2] == 78 && bArr[3] == 71 && bArr[4] == 13 && bArr[5] == 10 && bArr[6] == 26 && bArr[7] == 10) {
                try {
                    fileInputStream.close();
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                return "png";
            }
            if (b == 31 && bArr[1] == -117) {
                try {
                    fileInputStream.close();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
                return "tgs";
            }
            String lowerCase = new String(bArr).toLowerCase();
            if (lowerCase.startsWith("riff") && lowerCase.endsWith("webp")) {
                try {
                    fileInputStream.close();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
                return "webp";
            }
        }
        try {
            fileInputStream.close();
        } catch (Exception e6) {
            FileLog.e(e6);
        }
        return null;
    }

    public static boolean isWebp(Uri uri) {
        InputStream inputStreamOpenInputStream = null;
        try {
            try {
                inputStreamOpenInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                byte[] bArr = new byte[12];
                if (inputStreamOpenInputStream.read(bArr, 0, 12) == 12) {
                    String lowerCase = new String(bArr).toLowerCase();
                    if (lowerCase.startsWith("riff") && lowerCase.endsWith("webp")) {
                        try {
                            inputStreamOpenInputStream.close();
                            return true;
                        } catch (Exception e) {
                            FileLog.e(e);
                            return true;
                        }
                    }
                }
            } catch (Exception e2) {
                FileLog.e(e2);
                if (inputStreamOpenInputStream != null) {
                }
                return false;
            }
            try {
                inputStreamOpenInputStream.close();
            } catch (Exception e3) {
                FileLog.e(e3);
            }
            return false;
        } catch (Throwable th) {
            if (inputStreamOpenInputStream != null) {
                try {
                    inputStreamOpenInputStream.close();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            throw th;
        }
    }

    public static boolean isGif(Uri uri) {
        InputStream inputStreamOpenInputStream = null;
        try {
            try {
                inputStreamOpenInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                byte[] bArr = new byte[3];
                if (inputStreamOpenInputStream.read(bArr, 0, 3) == 3 && new String(bArr).equalsIgnoreCase("gif")) {
                    try {
                        inputStreamOpenInputStream.close();
                        return true;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return true;
                    }
                }
            } catch (Exception e2) {
                FileLog.e(e2);
                if (inputStreamOpenInputStream != null) {
                }
                return false;
            }
            try {
                inputStreamOpenInputStream.close();
            } catch (Exception e3) {
                FileLog.e(e3);
            }
            return false;
        } catch (Throwable th) {
            if (inputStreamOpenInputStream != null) {
                try {
                    inputStreamOpenInputStream.close();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:35:0x0058 A[Catch: Exception -> 0x0052, TryCatch #4 {Exception -> 0x0052, blocks: (B:5:0x0007, B:35:0x0058, B:37:0x0065, B:30:0x004e), top: B:48:0x0007 }] */
    /* JADX WARN: Code duplicated, block: B:37:0x0065 A[Catch: Exception -> 0x0052, TRY_LEAVE, TryCatch #4 {Exception -> 0x0052, blocks: (B:5:0x0007, B:35:0x0058, B:37:0x0065, B:30:0x004e), top: B:48:0x0007 }] */
    /* JADX WARN: Code duplicated, block: B:52:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:53:? A[RETURN, SYNTHETIC] */
    public static String getFileName(Uri uri) {
        Uri uri2;
        String path;
        int iLastIndexOf;
        if (uri == null) {
            return _UrlKt.FRAGMENT_ENCODE_SET;
        }
        try {
            String string = null;
            if (uri.getScheme().equals("content")) {
                try {
                    uri2 = uri;
                    try {
                        Cursor cursorQuery = ApplicationLoader.applicationContext.getContentResolver().query(uri2, new String[]{"_display_name"}, null, null, null);
                        try {
                            if (cursorQuery.moveToFirst()) {
                                string = cursorQuery.getString(cursorQuery.getColumnIndex("_display_name"));
                            }
                            cursorQuery.close();
                        } catch (Throwable th) {
                            if (cursorQuery == null) {
                                throw th;
                            }
                            try {
                                cursorQuery.close();
                                throw th;
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                                throw th;
                            }
                            if (string == null) {
                                return string;
                            }
                            path = uri2.getPath();
                            iLastIndexOf = path.lastIndexOf(47);
                            if (iLastIndexOf != -1) {
                                return path.substring(iLastIndexOf + 1);
                            }
                            return path;
                        }
                    } catch (Exception e) {
                        e = e;
                        FileLog.e(e);
                    }
                } catch (Exception e2) {
                    e = e2;
                    uri2 = uri;
                }
            } else {
                uri2 = uri;
            }
            if (string == null) {
                return string;
            }
            path = uri2.getPath();
            iLastIndexOf = path.lastIndexOf(47);
            if (iLastIndexOf != -1) {
                return path.substring(iLastIndexOf + 1);
            }
            return path;
        } catch (Exception e3) {
            FileLog.e(e3);
            return _UrlKt.FRAGMENT_ENCODE_SET;
        }
    }

    public static File createFileInCache(String str, String str2) {
        File file;
        try {
            File sharingDirectory = AndroidUtilities.getSharingDirectory();
            sharingDirectory.mkdirs();
            if (AndroidUtilities.isInternalUri(Uri.fromFile(sharingDirectory))) {
                return null;
            }
            int i = 0;
            do {
                File sharingDirectory2 = AndroidUtilities.getSharingDirectory();
                if (i == 0) {
                    file = new File(sharingDirectory2, str);
                } else {
                    int iLastIndexOf = str.lastIndexOf(".");
                    if (iLastIndexOf > 0) {
                        file = new File(sharingDirectory2, str.substring(0, iLastIndexOf) + " (" + i + ")" + str.substring(iLastIndexOf));
                    } else {
                        file = new File(sharingDirectory2, str + " (" + i + ")");
                    }
                }
                i++;
            } while (file.exists());
            return file;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static String copyFileToCache(Uri uri, String str) {
        return copyFileToCache(uri, str, -1L);
    }

    /* JADX WARN: Code duplicated, block: B:120:0x01a0  */
    /* JADX WARN: Code duplicated, block: B:138:0x01c6  */
    /* JADX WARN: Code duplicated, block: B:144:0x0188 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:148:0x0193 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:154:0x01b0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:158:0x01ba A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:181:? A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    @SuppressLint({"DiscouragedPrivateApi"})
    public static String copyFileToCache(Uri uri, String str, long j) throws Throwable {
        Throwable th;
        File sharingDirectory;
        FileOutputStream fileOutputStream;
        Exception exc;
        InputStream inputStream;
        int i;
        File file;
        File file2;
        InputStream inputStream2 = null;
        int i2 = 0;
        try {
            String strFixFileName = FileLoader.fixFileName(getFileName(uri));
            if (strFixFileName == null) {
                int lastLocalId = SharedConfig.getLastLocalId();
                SharedConfig.saveConfig();
                strFixFileName = String.format(Locale.US, "%d.%s", Integer.valueOf(lastLocalId), str);
            }
            sharingDirectory = AndroidUtilities.getSharingDirectory();
            try {
                sharingDirectory.mkdirs();
                if (AndroidUtilities.isInternalUri(Uri.fromFile(sharingDirectory))) {
                    if (j > 0 && 0 > j) {
                        sharingDirectory.delete();
                    }
                    return null;
                }
                int i3 = 0;
                do {
                    File sharingDirectory2 = AndroidUtilities.getSharingDirectory();
                    if (i3 == 0) {
                        file2 = new File(sharingDirectory2, strFixFileName);
                    } else {
                        int iLastIndexOf = strFixFileName.lastIndexOf(".");
                        if (iLastIndexOf > 0) {
                            sharingDirectory = new File(sharingDirectory2, strFixFileName.substring(0, iLastIndexOf) + " (" + i3 + ")" + strFixFileName.substring(iLastIndexOf));
                        } else {
                            file2 = new File(sharingDirectory2, strFixFileName + " (" + i3 + ")");
                        }
                        i3++;
                    }
                    sharingDirectory = file2;
                    i3++;
                } while (sharingDirectory.exists());
                InputStream inputStreamOpenInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                try {
                    if (inputStreamOpenInputStream instanceof FileInputStream) {
                        try {
                            if (AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", null).invoke(((FileInputStream) inputStreamOpenInputStream).getFD(), null)).intValue())) {
                                try {
                                    inputStreamOpenInputStream.close();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                if (j > 0 && 0 > j) {
                                    sharingDirectory.delete();
                                }
                                return null;
                            }
                        } catch (Throwable th2) {
                            FileLog.e(th2);
                        }
                    }
                    fileOutputStream = new FileOutputStream(sharingDirectory);
                    try {
                        byte[] bArr = new byte[20480];
                        i = 0;
                        while (true) {
                            try {
                                int i4 = inputStreamOpenInputStream.read(bArr);
                                if (i4 == -1) {
                                    String absolutePath = sharingDirectory.getAbsolutePath();
                                    try {
                                        inputStreamOpenInputStream.close();
                                    } catch (Exception e2) {
                                        FileLog.e(e2);
                                    }
                                    try {
                                        fileOutputStream.close();
                                    } catch (Exception e3) {
                                        FileLog.e(e3);
                                    }
                                    if (j > 0 && i > j) {
                                        sharingDirectory.delete();
                                    }
                                    return absolutePath;
                                }
                                fileOutputStream.write(bArr, 0, i4);
                                i += i4;
                                if (j > 0) {
                                    long j2 = i;
                                    if (j2 > j) {
                                        try {
                                            inputStreamOpenInputStream.close();
                                        } catch (Exception e4) {
                                            FileLog.e(e4);
                                        }
                                        try {
                                            fileOutputStream.close();
                                        } catch (Exception e5) {
                                            FileLog.e(e5);
                                        }
                                        if (j > 0 && j2 > j) {
                                            sharingDirectory.delete();
                                        }
                                        return null;
                                    }
                                }
                            } catch (Exception e6) {
                                e = e6;
                                file = sharingDirectory;
                                inputStream = inputStreamOpenInputStream;
                                exc = e;
                                FileLog.e(exc);
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Exception e7) {
                                        FileLog.e(e7);
                                    }
                                }
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Exception e8) {
                                        FileLog.e(e8);
                                    }
                                }
                                if (j > 0) {
                                    file.delete();
                                }
                                return null;
                            } catch (Throwable th3) {
                                inputStream2 = inputStreamOpenInputStream;
                                th = th3;
                                i2 = i;
                                if (inputStream2 != null) {
                                    try {
                                        inputStream2.close();
                                    } catch (Exception e9) {
                                        FileLog.e(e9);
                                    }
                                }
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Exception e10) {
                                        FileLog.e(e10);
                                    }
                                }
                                if (j > 0) {
                                    throw th;
                                }
                                throw th;
                            }
                        }
                    } catch (Exception e11) {
                        e = e11;
                        i = 0;
                        file = sharingDirectory;
                        inputStream = inputStreamOpenInputStream;
                        exc = e;
                        FileLog.e(exc);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (j > 0) {
                            file.delete();
                        }
                        return null;
                    } catch (Throwable th4) {
                        th = th4;
                        inputStream2 = inputStreamOpenInputStream;
                        th = th;
                        if (inputStream2 != null) {
                            inputStream2.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (j > 0) {
                            throw th;
                        }
                        throw th;
                    }
                } catch (Exception e12) {
                    e = e12;
                    fileOutputStream = null;
                } catch (Throwable th5) {
                    th = th5;
                    fileOutputStream = null;
                }
            } catch (Exception e13) {
                exc = e13;
                fileOutputStream = null;
                i = 0;
                file = sharingDirectory;
                inputStream = null;
            } catch (Throwable th6) {
                th = th6;
                fileOutputStream = null;
            }
        } catch (Exception e14) {
            exc = e14;
            inputStream = null;
            fileOutputStream = null;
            i = 0;
            file = null;
        } catch (Throwable th7) {
            th = th7;
            sharingDirectory = null;
            fileOutputStream = null;
        }
        try {
            FileLog.e(exc);
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (j > 0 && i > j) {
                file.delete();
            }
            return null;
        } catch (Throwable th8) {
            th = th8;
            inputStream2 = inputStream;
            sharingDirectory = file;
            i2 = i;
            if (inputStream2 != null) {
                inputStream2.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (j > 0 || i2 <= j) {
                throw th;
            }
            sharingDirectory.delete();
            throw th;
        }
    }

    public static void loadGalleryPhotosAlbums(final int i) {
        Thread thread = new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.$r8$lambda$JmMygXm9PMe62rd5ZbcMyTwrpTw(i);
            }
        });
        thread.setPriority(1);
        thread.start();
    }

    /* JADX WARN: Code duplicated, block: B:114:0x0282 A[Catch: all -> 0x0361, TryCatch #1 {all -> 0x0361, blocks: (B:112:0x027a, B:114:0x0282, B:118:0x029a, B:120:0x02ac, B:124:0x02cd, B:125:0x02ec, B:127:0x02f2, B:130:0x02fd, B:132:0x033a), top: B:207:0x027a }] */
    /* JADX WARN: Code duplicated, block: B:116:0x0295  */
    /* JADX WARN: Code duplicated, block: B:117:0x0298  */
    /* JADX WARN: Code duplicated, block: B:120:0x02ac A[Catch: all -> 0x0361, TryCatch #1 {all -> 0x0361, blocks: (B:112:0x027a, B:114:0x0282, B:118:0x029a, B:120:0x02ac, B:124:0x02cd, B:125:0x02ec, B:127:0x02f2, B:130:0x02fd, B:132:0x033a), top: B:207:0x027a }] */
    /* JADX WARN: Code duplicated, block: B:122:0x02c8  */
    /* JADX WARN: Code duplicated, block: B:123:0x02cb  */
    /* JADX WARN: Code duplicated, block: B:127:0x02f2 A[Catch: all -> 0x0361, TryCatch #1 {all -> 0x0361, blocks: (B:112:0x027a, B:114:0x0282, B:118:0x029a, B:120:0x02ac, B:124:0x02cd, B:125:0x02ec, B:127:0x02f2, B:130:0x02fd, B:132:0x033a), top: B:207:0x027a }] */
    /* JADX WARN: Code duplicated, block: B:132:0x033a A[Catch: all -> 0x0361, TRY_LEAVE, TryCatch #1 {all -> 0x0361, blocks: (B:112:0x027a, B:114:0x0282, B:118:0x029a, B:120:0x02ac, B:124:0x02cd, B:125:0x02ec, B:127:0x02f2, B:130:0x02fd, B:132:0x033a), top: B:207:0x027a }] */
    /* JADX WARN: Code duplicated, block: B:136:0x0351  */
    /* JADX WARN: Code duplicated, block: B:137:0x0352  */
    /* JADX WARN: Code duplicated, block: B:139:0x0355 A[Catch: all -> 0x035b, TryCatch #19 {all -> 0x035b, blocks: (B:134:0x034d, B:139:0x0355, B:140:0x0357, B:148:0x036d), top: B:243:0x034d }] */
    /* JADX WARN: Code duplicated, block: B:146:0x0363  */
    /* JADX WARN: Code duplicated, block: B:148:0x036d A[Catch: all -> 0x035b, TRY_LEAVE, TryCatch #19 {all -> 0x035b, blocks: (B:134:0x034d, B:139:0x0355, B:140:0x0357, B:148:0x036d), top: B:243:0x034d }] */
    /* JADX WARN: Code duplicated, block: B:153:0x0385  */
    /* JADX WARN: Code duplicated, block: B:156:0x0397 A[Catch: all -> 0x03be, TryCatch #22 {all -> 0x03be, blocks: (B:154:0x0389, B:156:0x0397, B:160:0x03a5), top: B:249:0x0389 }] */
    /* JADX WARN: Code duplicated, block: B:158:0x03a1 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:166:0x03bc  */
    /* JADX WARN: Code duplicated, block: B:170:0x03c5  */
    /* JADX WARN: Code duplicated, block: B:186:0x03fb A[LOOP:1: B:184:0x03f5->B:186:0x03fb, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:205:0x026b A[EXC_TOP_SPLITTER, PHI: r11 r21 r22 r23 r24 r25 r26 r28 r29 r30 r31
  0x026b: PHI (r11v14 android.database.Cursor) = (r11v3 android.database.Cursor), (r11v16 android.database.Cursor) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r21v9 java.lang.String) = (r21v2 java.lang.String), (r21v11 java.lang.String) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r22v7 java.lang.String) = (r22v2 java.lang.String), (r22v9 java.lang.String) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r23v7 java.lang.String) = (r23v2 java.lang.String), (r23v9 java.lang.String) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r24v4 java.lang.String) = (r24v2 java.lang.String), (r24v6 java.lang.String) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r25v4 java.lang.String) = (r25v2 java.lang.String), (r25v6 java.lang.String) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r26v4 java.lang.String) = (r26v2 java.lang.String), (r26v6 java.lang.String) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r28v4 java.lang.String) = (r28v2 java.lang.String), (r28v6 java.lang.String) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r29v4 org.telegram.messenger.MediaController$AlbumEntry) = (r29v2 org.telegram.messenger.MediaController$AlbumEntry), (r29v6 org.telegram.messenger.MediaController$AlbumEntry) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r30v14 org.telegram.messenger.MediaController$AlbumEntry) = (r30v1 org.telegram.messenger.MediaController$AlbumEntry), (r30v15 org.telegram.messenger.MediaController$AlbumEntry) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE]
  0x026b: PHI (r31v15 java.lang.Object) = (r31v1 java.lang.Object), (r31v16 java.lang.Object) binds: [B:110:0x0277, B:104:0x0269] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:245:0x03df A[EXC_TOP_SPLITTER, PHI: r6 r11 r16 r30 r31
  0x03df: PHI (r6v2 int) = (r6v4 int), (r6v5 int) binds: [B:181:0x03f1, B:174:0x03dd] A[DONT_GENERATE, DONT_INLINE]
  0x03df: PHI (r11v7 android.database.Cursor) = (r11v11 android.database.Cursor), (r11v12 android.database.Cursor) binds: [B:181:0x03f1, B:174:0x03dd] A[DONT_GENERATE, DONT_INLINE]
  0x03df: PHI (r16v3 org.telegram.messenger.MediaController$AlbumEntry) = (r16v6 org.telegram.messenger.MediaController$AlbumEntry), (r16v7 org.telegram.messenger.MediaController$AlbumEntry) binds: [B:181:0x03f1, B:174:0x03dd] A[DONT_GENERATE, DONT_INLINE]
  0x03df: PHI (r30v5 org.telegram.messenger.MediaController$AlbumEntry) = (r30v7 org.telegram.messenger.MediaController$AlbumEntry), (r30v8 org.telegram.messenger.MediaController$AlbumEntry) binds: [B:181:0x03f1, B:174:0x03dd] A[DONT_GENERATE, DONT_INLINE]
  0x03df: PHI (r31v5 java.lang.Object) = (r31v7 java.lang.Object), (r31v8 java.lang.Object) binds: [B:181:0x03f1, B:174:0x03dd] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:255:0x02fd A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:257:0x02fc A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void $r8$lambda$JmMygXm9PMe62rd5ZbcMyTwrpTw(int i) {
        String str;
        String string;
        Cursor cursorQuery;
        AlbumEntry albumEntry;
        AlbumEntry albumEntry2;
        Object objValueOf;
        int i2;
        int i3;
        int i4;
        String str2;
        int columnIndex;
        int columnIndex2;
        int columnIndex3;
        int columnIndex4;
        String str3;
        int columnIndex5;
        int columnIndex6;
        int columnIndex7;
        int columnIndex8;
        int columnIndex9;
        String string2;
        int i5;
        String string3;
        PhotoEntry photoEntry;
        AlbumEntry albumEntry3;
        AlbumEntry albumEntry4;
        AlbumEntry albumEntry5;
        int i6;
        AlbumEntry albumEntry6;
        AlbumEntry albumEntry7;
        String str4 = "height";
        String str5 = "width";
        String str6 = "orientation";
        String str7 = "_data";
        String str8 = "bucket_display_name";
        String str9 = "bucket_id";
        String str10 = "_id";
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        SparseArray sparseArray = new SparseArray();
        SparseArray sparseArray2 = new SparseArray();
        AlbumEntry albumEntry8 = null;
        try {
            StringBuilder sb = new StringBuilder();
            str = "datetaken";
            try {
                sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
                sb.append("/Camera/");
                string = sb.toString();
            } catch (Exception e) {
                e = e;
                FileLog.e(e);
                string = null;
            }
        } catch (Exception e2) {
            e = e2;
            str = "datetaken";
        }
        try {
            try {
                int i7 = Build.VERSION.SDK_INT;
                if (SystemUtils.isImagesPermissionGranted()) {
                    ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    String[] strArr = projectionPhotos;
                    StringBuilder sb2 = new StringBuilder();
                    SparseArray sparseArray3 = sparseArray2;
                    sb2.append(i7 > 28 ? "date_modified" : str);
                    sb2.append(" DESC");
                    cursorQuery = MediaStore.Images.Media.query(contentResolver, uri, strArr, null, null, sb2.toString());
                    if (cursorQuery != null) {
                        try {
                            int columnIndex10 = cursorQuery.getColumnIndex("_id");
                            str10 = "_id";
                            try {
                                int columnIndex11 = cursorQuery.getColumnIndex("bucket_id");
                                str9 = "bucket_id";
                                try {
                                    int columnIndex12 = cursorQuery.getColumnIndex("bucket_display_name");
                                    str8 = "bucket_display_name";
                                    try {
                                        int columnIndex13 = cursorQuery.getColumnIndex("_data");
                                        str7 = "_data";
                                        try {
                                            int columnIndex14 = cursorQuery.getColumnIndex(i7 > 28 ? "date_modified" : str);
                                            int columnIndex15 = cursorQuery.getColumnIndex("orientation");
                                            str6 = "orientation";
                                            try {
                                                int columnIndex16 = cursorQuery.getColumnIndex("width");
                                                str5 = "width";
                                                try {
                                                    int columnIndex17 = cursorQuery.getColumnIndex("height");
                                                    str4 = "height";
                                                    try {
                                                        int columnIndex18 = cursorQuery.getColumnIndex("_size");
                                                        albumEntry = null;
                                                        albumEntry2 = null;
                                                        objValueOf = null;
                                                        Integer numValueOf = null;
                                                        while (cursorQuery.moveToNext()) {
                                                            try {
                                                                String string4 = cursorQuery.getString(columnIndex13);
                                                                if (!TextUtils.isEmpty(string4)) {
                                                                    int i8 = cursorQuery.getInt(columnIndex10);
                                                                    int i9 = cursorQuery.getInt(columnIndex11);
                                                                    int i10 = columnIndex13;
                                                                    String string5 = cursorQuery.getString(columnIndex12);
                                                                    PhotoEntry photoEntry2 = new PhotoEntry(i9, i8, cursorQuery.getLong(columnIndex14), string4, cursorQuery.getInt(columnIndex15), 0, false, cursorQuery.getInt(columnIndex16), cursorQuery.getInt(columnIndex17), cursorQuery.getLong(columnIndex18));
                                                                    int i11 = columnIndex18;
                                                                    int i12 = columnIndex17;
                                                                    int i13 = columnIndex14;
                                                                    if (albumEntry == null) {
                                                                        albumEntry6 = new AlbumEntry(0, LocaleController.getString(R.string.AllPhotos), photoEntry2);
                                                                        try {
                                                                            arrayList2.add(0, albumEntry6);
                                                                        } catch (Throwable th) {
                                                                            th = th;
                                                                            albumEntry = albumEntry6;
                                                                            try {
                                                                                FileLog.e(th);
                                                                                if (cursorQuery != null) {
                                                                                    try {
                                                                                        cursorQuery.close();
                                                                                    } catch (Exception e3) {
                                                                                        FileLog.e(e3);
                                                                                    }
                                                                                }
                                                                                i4 = Build.VERSION.SDK_INT;
                                                                                if (SystemUtils.isVideoPermissionGranted()) {
                                                                                    ContentResolver contentResolver2 = ApplicationLoader.applicationContext.getContentResolver();
                                                                                    Uri uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                                                    String[] strArr2 = projectionVideo;
                                                                                    StringBuilder sb3 = new StringBuilder();
                                                                                    if (i4 > 28) {
                                                                                        str2 = "date_modified";
                                                                                    } else {
                                                                                        str2 = str;
                                                                                    }
                                                                                    sb3.append(str2);
                                                                                    sb3.append(" DESC");
                                                                                    cursorQuery = MediaStore.Images.Media.query(contentResolver2, uri2, strArr2, null, null, sb3.toString());
                                                                                    if (cursorQuery != null) {
                                                                                        columnIndex = cursorQuery.getColumnIndex(str10);
                                                                                        columnIndex2 = cursorQuery.getColumnIndex(str9);
                                                                                        columnIndex3 = cursorQuery.getColumnIndex(str8);
                                                                                        columnIndex4 = cursorQuery.getColumnIndex(str7);
                                                                                        if (i4 > 28) {
                                                                                            str3 = "date_modified";
                                                                                        } else {
                                                                                            str3 = str;
                                                                                        }
                                                                                        columnIndex5 = cursorQuery.getColumnIndex(str3);
                                                                                        columnIndex6 = cursorQuery.getColumnIndex("duration");
                                                                                        columnIndex7 = cursorQuery.getColumnIndex(str5);
                                                                                        columnIndex8 = cursorQuery.getColumnIndex(str4);
                                                                                        columnIndex9 = cursorQuery.getColumnIndex("_size");
                                                                                        cursorQuery.getColumnIndex(str6);
                                                                                        while (cursorQuery.moveToNext()) {
                                                                                            string2 = cursorQuery.getString(columnIndex4);
                                                                                            if (!TextUtils.isEmpty(string2)) {
                                                                                                int i14 = cursorQuery.getInt(columnIndex);
                                                                                                i5 = cursorQuery.getInt(columnIndex2);
                                                                                                string3 = cursorQuery.getString(columnIndex3);
                                                                                                int i15 = columnIndex5;
                                                                                                int i16 = columnIndex9;
                                                                                                photoEntry = new PhotoEntry(i5, i14, cursorQuery.getLong(columnIndex5), string2, 0, (int) (cursorQuery.getLong(columnIndex6) / 1000), true, cursorQuery.getInt(columnIndex7), cursorQuery.getInt(columnIndex8), cursorQuery.getLong(columnIndex9));
                                                                                                int i17 = columnIndex;
                                                                                                if (albumEntry8 == null) {
                                                                                                    albumEntry3 = new AlbumEntry(0, LocaleController.getString(R.string.AllVideos), photoEntry);
                                                                                                    i6 = 1;
                                                                                                    try {
                                                                                                        albumEntry3.videoOnly = true;
                                                                                                        if (albumEntry2 == null) {
                                                                                                            i6 = 0;
                                                                                                        }
                                                                                                        if (albumEntry != null) {
                                                                                                            i6++;
                                                                                                        }
                                                                                                        arrayList.add(i6, albumEntry3);
                                                                                                    } catch (Throwable th2) {
                                                                                                        th = th2;
                                                                                                        albumEntry8 = albumEntry3;
                                                                                                        i2 = 0;
                                                                                                        try {
                                                                                                            FileLog.e(th);
                                                                                                            if (cursorQuery != null) {
                                                                                                                try {
                                                                                                                    cursorQuery.close();
                                                                                                                } catch (Exception e4) {
                                                                                                                    FileLog.e(e4);
                                                                                                                }
                                                                                                            }
                                                                                                            AlbumEntry albumEntry9 = albumEntry8;
                                                                                                            AlbumEntry albumEntry10 = albumEntry2;
                                                                                                            Integer num = objValueOf;
                                                                                                            for (i3 = i2; i3 < arrayList.size(); i3++) {
                                                                                                                Collections.sort(((AlbumEntry) arrayList.get(i3)).photos, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda42
                                                                                                                    @Override // java.util.Comparator
                                                                                                                    public final int compare(Object obj, Object obj2) {
                                                                                                                        return MediaController.m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE((MediaController.PhotoEntry) obj, (MediaController.PhotoEntry) obj2);
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                            broadcastNewPhotos(i, arrayList, arrayList2, num, albumEntry10, albumEntry, albumEntry9, 0);
                                                                                                        } catch (Throwable th3) {
                                                                                                            if (cursorQuery != null) {
                                                                                                                try {
                                                                                                                    cursorQuery.close();
                                                                                                                    throw th3;
                                                                                                                } catch (Exception e5) {
                                                                                                                    FileLog.e(e5);
                                                                                                                    throw th3;
                                                                                                                }
                                                                                                            }
                                                                                                            throw th3;
                                                                                                        }
                                                                                                    }
                                                                                                } else {
                                                                                                    albumEntry3 = albumEntry8;
                                                                                                }
                                                                                                if (albumEntry2 == null) {
                                                                                                    albumEntry4 = new AlbumEntry(0, LocaleController.getString(R.string.AllMedia), photoEntry);
                                                                                                    try {
                                                                                                        arrayList.add(0, albumEntry4);
                                                                                                    } catch (Throwable th4) {
                                                                                                        th = th4;
                                                                                                        albumEntry8 = albumEntry3;
                                                                                                        albumEntry2 = albumEntry4;
                                                                                                        i2 = 0;
                                                                                                        FileLog.e(th);
                                                                                                        if (cursorQuery != null) {
                                                                                                            cursorQuery.close();
                                                                                                        }
                                                                                                        AlbumEntry albumEntry11 = albumEntry8;
                                                                                                        AlbumEntry albumEntry12 = albumEntry2;
                                                                                                        Integer num2 = objValueOf;
                                                                                                        while (i3 < arrayList.size()) {
                                                                                                            Collections.sort(((AlbumEntry) arrayList.get(i3)).photos, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda42
                                                                                                                @Override // java.util.Comparator
                                                                                                                public final int compare(Object obj, Object obj2) {
                                                                                                                    return MediaController.m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE((MediaController.PhotoEntry) obj, (MediaController.PhotoEntry) obj2);
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                        broadcastNewPhotos(i, arrayList, arrayList2, num2, albumEntry12, albumEntry, albumEntry11, 0);
                                                                                                    }
                                                                                                } else {
                                                                                                    albumEntry4 = albumEntry2;
                                                                                                }
                                                                                                try {
                                                                                                    albumEntry3.addPhoto(photoEntry);
                                                                                                    albumEntry4.addPhoto(photoEntry);
                                                                                                    albumEntry5 = (AlbumEntry) sparseArray.get(i5);
                                                                                                    if (albumEntry5 == null) {
                                                                                                        albumEntry5 = new AlbumEntry(i5, string3, photoEntry);
                                                                                                        sparseArray.put(i5, albumEntry5);
                                                                                                        if (objValueOf != null) {
                                                                                                            arrayList.add(albumEntry5);
                                                                                                        } else {
                                                                                                            arrayList.add(albumEntry5);
                                                                                                        }
                                                                                                    }
                                                                                                    albumEntry5.addPhoto(photoEntry);
                                                                                                    albumEntry8 = albumEntry3;
                                                                                                    albumEntry2 = albumEntry4;
                                                                                                    columnIndex5 = i15;
                                                                                                    columnIndex = i17;
                                                                                                    columnIndex2 = columnIndex2;
                                                                                                    columnIndex9 = i16;
                                                                                                    columnIndex4 = columnIndex4;
                                                                                                    columnIndex3 = columnIndex3;
                                                                                                    columnIndex6 = columnIndex6;
                                                                                                } catch (Throwable th5) {
                                                                                                    th = th5;
                                                                                                    i2 = 0;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                i2 = 0;
                                                                                if (cursorQuery != null) {
                                                                                    cursorQuery.close();
                                                                                }
                                                                                AlbumEntry albumEntry13 = albumEntry8;
                                                                                AlbumEntry albumEntry14 = albumEntry2;
                                                                                Integer num3 = objValueOf;
                                                                                while (i3 < arrayList.size()) {
                                                                                    Collections.sort(((AlbumEntry) arrayList.get(i3)).photos, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda42
                                                                                        @Override // java.util.Comparator
                                                                                        public final int compare(Object obj, Object obj2) {
                                                                                            return MediaController.m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE((MediaController.PhotoEntry) obj, (MediaController.PhotoEntry) obj2);
                                                                                        }
                                                                                    });
                                                                                }
                                                                                broadcastNewPhotos(i, arrayList, arrayList2, num3, albumEntry14, albumEntry, albumEntry13, 0);
                                                                            } catch (Throwable th6) {
                                                                                if (cursorQuery != null) {
                                                                                    try {
                                                                                        cursorQuery.close();
                                                                                        throw th6;
                                                                                    } catch (Exception e6) {
                                                                                        FileLog.e(e6);
                                                                                        throw th6;
                                                                                    }
                                                                                }
                                                                                throw th6;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        albumEntry6 = albumEntry;
                                                                    }
                                                                    if (albumEntry2 == null) {
                                                                        albumEntry7 = new AlbumEntry(0, LocaleController.getString(R.string.AllMedia), photoEntry2);
                                                                        try {
                                                                            arrayList.add(0, albumEntry7);
                                                                        } catch (Throwable th7) {
                                                                            th = th7;
                                                                            albumEntry = albumEntry6;
                                                                            albumEntry2 = albumEntry7;
                                                                            FileLog.e(th);
                                                                            if (cursorQuery != null) {
                                                                                cursorQuery.close();
                                                                            }
                                                                            i4 = Build.VERSION.SDK_INT;
                                                                            if (SystemUtils.isVideoPermissionGranted()) {
                                                                                ContentResolver contentResolver3 = ApplicationLoader.applicationContext.getContentResolver();
                                                                                Uri uri3 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                                                String[] strArr3 = projectionVideo;
                                                                                StringBuilder sb4 = new StringBuilder();
                                                                                if (i4 > 28) {
                                                                                    str2 = "date_modified";
                                                                                } else {
                                                                                    str2 = str;
                                                                                }
                                                                                sb4.append(str2);
                                                                                sb4.append(" DESC");
                                                                                cursorQuery = MediaStore.Images.Media.query(contentResolver3, uri3, strArr3, null, null, sb4.toString());
                                                                                if (cursorQuery != null) {
                                                                                    columnIndex = cursorQuery.getColumnIndex(str10);
                                                                                    columnIndex2 = cursorQuery.getColumnIndex(str9);
                                                                                    columnIndex3 = cursorQuery.getColumnIndex(str8);
                                                                                    columnIndex4 = cursorQuery.getColumnIndex(str7);
                                                                                    if (i4 > 28) {
                                                                                        str3 = "date_modified";
                                                                                    } else {
                                                                                        str3 = str;
                                                                                    }
                                                                                    columnIndex5 = cursorQuery.getColumnIndex(str3);
                                                                                    columnIndex6 = cursorQuery.getColumnIndex("duration");
                                                                                    columnIndex7 = cursorQuery.getColumnIndex(str5);
                                                                                    columnIndex8 = cursorQuery.getColumnIndex(str4);
                                                                                    columnIndex9 = cursorQuery.getColumnIndex("_size");
                                                                                    cursorQuery.getColumnIndex(str6);
                                                                                    while (cursorQuery.moveToNext()) {
                                                                                        string2 = cursorQuery.getString(columnIndex4);
                                                                                        if (!TextUtils.isEmpty(string2)) {
                                                                                            int i18 = cursorQuery.getInt(columnIndex);
                                                                                            i5 = cursorQuery.getInt(columnIndex2);
                                                                                            string3 = cursorQuery.getString(columnIndex3);
                                                                                            int i19 = columnIndex5;
                                                                                            int i110 = columnIndex9;
                                                                                            photoEntry = new PhotoEntry(i5, i18, cursorQuery.getLong(columnIndex5), string2, 0, (int) (cursorQuery.getLong(columnIndex6) / 1000), true, cursorQuery.getInt(columnIndex7), cursorQuery.getInt(columnIndex8), cursorQuery.getLong(columnIndex9));
                                                                                            int i111 = columnIndex;
                                                                                            if (albumEntry8 == null) {
                                                                                                albumEntry3 = new AlbumEntry(0, LocaleController.getString(R.string.AllVideos), photoEntry);
                                                                                                i6 = 1;
                                                                                                albumEntry3.videoOnly = true;
                                                                                                if (albumEntry2 == null) {
                                                                                                    i6 = 0;
                                                                                                }
                                                                                                if (albumEntry != null) {
                                                                                                    i6++;
                                                                                                }
                                                                                                arrayList.add(i6, albumEntry3);
                                                                                            } else {
                                                                                                albumEntry3 = albumEntry8;
                                                                                            }
                                                                                            if (albumEntry2 == null) {
                                                                                                albumEntry4 = new AlbumEntry(0, LocaleController.getString(R.string.AllMedia), photoEntry);
                                                                                                arrayList.add(0, albumEntry4);
                                                                                            } else {
                                                                                                albumEntry4 = albumEntry2;
                                                                                            }
                                                                                            albumEntry3.addPhoto(photoEntry);
                                                                                            albumEntry4.addPhoto(photoEntry);
                                                                                            albumEntry5 = (AlbumEntry) sparseArray.get(i5);
                                                                                            if (albumEntry5 == null) {
                                                                                                albumEntry5 = new AlbumEntry(i5, string3, photoEntry);
                                                                                                sparseArray.put(i5, albumEntry5);
                                                                                                if (objValueOf != null) {
                                                                                                    arrayList.add(albumEntry5);
                                                                                                } else {
                                                                                                    arrayList.add(albumEntry5);
                                                                                                }
                                                                                            }
                                                                                            albumEntry5.addPhoto(photoEntry);
                                                                                            albumEntry8 = albumEntry3;
                                                                                            albumEntry2 = albumEntry4;
                                                                                            columnIndex5 = i19;
                                                                                            columnIndex = i111;
                                                                                            columnIndex2 = columnIndex2;
                                                                                            columnIndex9 = i110;
                                                                                            columnIndex4 = columnIndex4;
                                                                                            columnIndex3 = columnIndex3;
                                                                                            columnIndex6 = columnIndex6;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            i2 = 0;
                                                                            if (cursorQuery != null) {
                                                                                cursorQuery.close();
                                                                            }
                                                                            AlbumEntry albumEntry15 = albumEntry8;
                                                                            AlbumEntry albumEntry16 = albumEntry2;
                                                                            Integer num4 = objValueOf;
                                                                            while (i3 < arrayList.size()) {
                                                                                Collections.sort(((AlbumEntry) arrayList.get(i3)).photos, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda42
                                                                                    @Override // java.util.Comparator
                                                                                    public final int compare(Object obj, Object obj2) {
                                                                                        return MediaController.m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE((MediaController.PhotoEntry) obj, (MediaController.PhotoEntry) obj2);
                                                                                    }
                                                                                });
                                                                            }
                                                                            broadcastNewPhotos(i, arrayList, arrayList2, num4, albumEntry16, albumEntry, albumEntry15, 0);
                                                                        }
                                                                    } else {
                                                                        albumEntry7 = albumEntry2;
                                                                    }
                                                                    albumEntry6.addPhoto(photoEntry2);
                                                                    albumEntry7.addPhoto(photoEntry2);
                                                                    AlbumEntry albumEntry17 = (AlbumEntry) sparseArray.get(i9);
                                                                    if (albumEntry17 == null) {
                                                                        albumEntry17 = new AlbumEntry(i9, string5, photoEntry2);
                                                                        sparseArray.put(i9, albumEntry17);
                                                                        if (objValueOf == null && string != null && string4 != null && string4.startsWith(string)) {
                                                                            arrayList.add(0, albumEntry17);
                                                                            objValueOf = Integer.valueOf(i9);
                                                                        } else {
                                                                            arrayList.add(albumEntry17);
                                                                        }
                                                                    }
                                                                    albumEntry17.addPhoto(photoEntry2);
                                                                    SparseArray sparseArray4 = sparseArray3;
                                                                    AlbumEntry albumEntry18 = (AlbumEntry) sparseArray4.get(i9);
                                                                    if (albumEntry18 == null) {
                                                                        albumEntry18 = new AlbumEntry(i9, string5, photoEntry2);
                                                                        sparseArray4.put(i9, albumEntry18);
                                                                        if (numValueOf == null && string != null && string4 != null && string4.startsWith(string)) {
                                                                            arrayList2.add(0, albumEntry18);
                                                                            numValueOf = Integer.valueOf(i9);
                                                                        } else {
                                                                            arrayList2.add(albumEntry18);
                                                                        }
                                                                    }
                                                                    albumEntry18.addPhoto(photoEntry2);
                                                                    albumEntry = albumEntry6;
                                                                    albumEntry2 = albumEntry7;
                                                                    sparseArray3 = sparseArray4;
                                                                    columnIndex13 = i10;
                                                                    columnIndex18 = i11;
                                                                    columnIndex17 = i12;
                                                                    columnIndex14 = i13;
                                                                    columnIndex16 = columnIndex16;
                                                                    columnIndex12 = columnIndex12;
                                                                    columnIndex15 = columnIndex15;
                                                                    columnIndex11 = columnIndex11;
                                                                }
                                                            } catch (Throwable th8) {
                                                                th = th8;
                                                            }
                                                        }
                                                    } catch (Throwable th9) {
                                                        th = th9;
                                                        albumEntry = null;
                                                        albumEntry2 = albumEntry;
                                                        objValueOf = albumEntry2;
                                                        FileLog.e(th);
                                                        if (cursorQuery != null) {
                                                            cursorQuery.close();
                                                        }
                                                        i4 = Build.VERSION.SDK_INT;
                                                        if (SystemUtils.isVideoPermissionGranted()) {
                                                            ContentResolver contentResolver4 = ApplicationLoader.applicationContext.getContentResolver();
                                                            Uri uri4 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                            String[] strArr4 = projectionVideo;
                                                            StringBuilder sb5 = new StringBuilder();
                                                            if (i4 > 28) {
                                                                str2 = "date_modified";
                                                            } else {
                                                                str2 = str;
                                                            }
                                                            sb5.append(str2);
                                                            sb5.append(" DESC");
                                                            cursorQuery = MediaStore.Images.Media.query(contentResolver4, uri4, strArr4, null, null, sb5.toString());
                                                            if (cursorQuery != null) {
                                                                columnIndex = cursorQuery.getColumnIndex(str10);
                                                                columnIndex2 = cursorQuery.getColumnIndex(str9);
                                                                columnIndex3 = cursorQuery.getColumnIndex(str8);
                                                                columnIndex4 = cursorQuery.getColumnIndex(str7);
                                                                if (i4 > 28) {
                                                                    str3 = "date_modified";
                                                                } else {
                                                                    str3 = str;
                                                                }
                                                                columnIndex5 = cursorQuery.getColumnIndex(str3);
                                                                columnIndex6 = cursorQuery.getColumnIndex("duration");
                                                                columnIndex7 = cursorQuery.getColumnIndex(str5);
                                                                columnIndex8 = cursorQuery.getColumnIndex(str4);
                                                                columnIndex9 = cursorQuery.getColumnIndex("_size");
                                                                cursorQuery.getColumnIndex(str6);
                                                                while (cursorQuery.moveToNext()) {
                                                                    string2 = cursorQuery.getString(columnIndex4);
                                                                    if (!TextUtils.isEmpty(string2)) {
                                                                        int i112 = cursorQuery.getInt(columnIndex);
                                                                        i5 = cursorQuery.getInt(columnIndex2);
                                                                        string3 = cursorQuery.getString(columnIndex3);
                                                                        int i113 = columnIndex5;
                                                                        int i114 = columnIndex9;
                                                                        photoEntry = new PhotoEntry(i5, i112, cursorQuery.getLong(columnIndex5), string2, 0, (int) (cursorQuery.getLong(columnIndex6) / 1000), true, cursorQuery.getInt(columnIndex7), cursorQuery.getInt(columnIndex8), cursorQuery.getLong(columnIndex9));
                                                                        int i115 = columnIndex;
                                                                        if (albumEntry8 == null) {
                                                                            albumEntry3 = new AlbumEntry(0, LocaleController.getString(R.string.AllVideos), photoEntry);
                                                                            i6 = 1;
                                                                            albumEntry3.videoOnly = true;
                                                                            if (albumEntry2 == null) {
                                                                                i6 = 0;
                                                                            }
                                                                            if (albumEntry != null) {
                                                                                i6++;
                                                                            }
                                                                            arrayList.add(i6, albumEntry3);
                                                                        } else {
                                                                            albumEntry3 = albumEntry8;
                                                                        }
                                                                        if (albumEntry2 == null) {
                                                                            albumEntry4 = new AlbumEntry(0, LocaleController.getString(R.string.AllMedia), photoEntry);
                                                                            arrayList.add(0, albumEntry4);
                                                                        } else {
                                                                            albumEntry4 = albumEntry2;
                                                                        }
                                                                        albumEntry3.addPhoto(photoEntry);
                                                                        albumEntry4.addPhoto(photoEntry);
                                                                        albumEntry5 = (AlbumEntry) sparseArray.get(i5);
                                                                        if (albumEntry5 == null) {
                                                                            albumEntry5 = new AlbumEntry(i5, string3, photoEntry);
                                                                            sparseArray.put(i5, albumEntry5);
                                                                            if (objValueOf != null) {
                                                                                arrayList.add(albumEntry5);
                                                                            } else {
                                                                                arrayList.add(albumEntry5);
                                                                            }
                                                                        }
                                                                        albumEntry5.addPhoto(photoEntry);
                                                                        albumEntry8 = albumEntry3;
                                                                        albumEntry2 = albumEntry4;
                                                                        columnIndex5 = i113;
                                                                        columnIndex = i115;
                                                                        columnIndex2 = columnIndex2;
                                                                        columnIndex9 = i114;
                                                                        columnIndex4 = columnIndex4;
                                                                        columnIndex3 = columnIndex3;
                                                                        columnIndex6 = columnIndex6;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        i2 = 0;
                                                        if (cursorQuery != null) {
                                                            cursorQuery.close();
                                                        }
                                                        AlbumEntry albumEntry19 = albumEntry8;
                                                        AlbumEntry albumEntry110 = albumEntry2;
                                                        Integer num5 = objValueOf;
                                                        while (i3 < arrayList.size()) {
                                                            Collections.sort(((AlbumEntry) arrayList.get(i3)).photos, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda42
                                                                @Override // java.util.Comparator
                                                                public final int compare(Object obj, Object obj2) {
                                                                    return MediaController.m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE((MediaController.PhotoEntry) obj, (MediaController.PhotoEntry) obj2);
                                                                }
                                                            });
                                                        }
                                                        broadcastNewPhotos(i, arrayList, arrayList2, num5, albumEntry110, albumEntry, albumEntry19, 0);
                                                    }
                                                } catch (Throwable th10) {
                                                    th = th10;
                                                    str4 = "height";
                                                }
                                            } catch (Throwable th11) {
                                                th = th11;
                                                str4 = "height";
                                                str5 = "width";
                                            }
                                        } catch (Throwable th12) {
                                            th = th12;
                                            str4 = "height";
                                            str5 = "width";
                                            str6 = "orientation";
                                        }
                                    } catch (Throwable th13) {
                                        th = th13;
                                        str4 = "height";
                                        str5 = "width";
                                        str6 = "orientation";
                                        str7 = "_data";
                                    }
                                } catch (Throwable th14) {
                                    th = th14;
                                    str4 = "height";
                                    str5 = "width";
                                    str6 = "orientation";
                                    str7 = "_data";
                                    str8 = "bucket_display_name";
                                }
                            } catch (Throwable th15) {
                                th = th15;
                                str4 = "height";
                                str5 = "width";
                                str6 = "orientation";
                                str7 = "_data";
                                str8 = "bucket_display_name";
                                str9 = "bucket_id";
                            }
                        } catch (Throwable th16) {
                            th = th16;
                            str4 = "height";
                            str5 = "width";
                            str6 = "orientation";
                            str7 = "_data";
                            str8 = "bucket_display_name";
                            str9 = "bucket_id";
                            str10 = "_id";
                        }
                    } else {
                        albumEntry = null;
                    }
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    i4 = Build.VERSION.SDK_INT;
                    if (SystemUtils.isVideoPermissionGranted()) {
                        ContentResolver contentResolver5 = ApplicationLoader.applicationContext.getContentResolver();
                        Uri uri5 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        String[] strArr5 = projectionVideo;
                        StringBuilder sb6 = new StringBuilder();
                        if (i4 > 28) {
                            str2 = "date_modified";
                        } else {
                            str2 = str;
                        }
                        sb6.append(str2);
                        sb6.append(" DESC");
                        cursorQuery = MediaStore.Images.Media.query(contentResolver5, uri5, strArr5, null, null, sb6.toString());
                        if (cursorQuery != null) {
                            columnIndex = cursorQuery.getColumnIndex(str10);
                            columnIndex2 = cursorQuery.getColumnIndex(str9);
                            columnIndex3 = cursorQuery.getColumnIndex(str8);
                            columnIndex4 = cursorQuery.getColumnIndex(str7);
                            if (i4 > 28) {
                                str3 = "date_modified";
                            } else {
                                str3 = str;
                            }
                            columnIndex5 = cursorQuery.getColumnIndex(str3);
                            columnIndex6 = cursorQuery.getColumnIndex("duration");
                            columnIndex7 = cursorQuery.getColumnIndex(str5);
                            columnIndex8 = cursorQuery.getColumnIndex(str4);
                            columnIndex9 = cursorQuery.getColumnIndex("_size");
                            cursorQuery.getColumnIndex(str6);
                            while (cursorQuery.moveToNext()) {
                                string2 = cursorQuery.getString(columnIndex4);
                                if (!TextUtils.isEmpty(string2)) {
                                    int i116 = cursorQuery.getInt(columnIndex);
                                    i5 = cursorQuery.getInt(columnIndex2);
                                    string3 = cursorQuery.getString(columnIndex3);
                                    int i117 = columnIndex5;
                                    int i118 = columnIndex9;
                                    photoEntry = new PhotoEntry(i5, i116, cursorQuery.getLong(columnIndex5), string2, 0, (int) (cursorQuery.getLong(columnIndex6) / 1000), true, cursorQuery.getInt(columnIndex7), cursorQuery.getInt(columnIndex8), cursorQuery.getLong(columnIndex9));
                                    int i119 = columnIndex;
                                    if (albumEntry8 == null) {
                                        albumEntry3 = new AlbumEntry(0, LocaleController.getString(R.string.AllVideos), photoEntry);
                                        i6 = 1;
                                        albumEntry3.videoOnly = true;
                                        if (albumEntry2 == null) {
                                            i6 = 0;
                                        }
                                        if (albumEntry != null) {
                                            i6++;
                                        }
                                        arrayList.add(i6, albumEntry3);
                                    } else {
                                        albumEntry3 = albumEntry8;
                                    }
                                    if (albumEntry2 == null) {
                                        albumEntry4 = new AlbumEntry(0, LocaleController.getString(R.string.AllMedia), photoEntry);
                                        arrayList.add(0, albumEntry4);
                                    } else {
                                        albumEntry4 = albumEntry2;
                                    }
                                    albumEntry3.addPhoto(photoEntry);
                                    albumEntry4.addPhoto(photoEntry);
                                    albumEntry5 = (AlbumEntry) sparseArray.get(i5);
                                    if (albumEntry5 == null) {
                                        albumEntry5 = new AlbumEntry(i5, string3, photoEntry);
                                        sparseArray.put(i5, albumEntry5);
                                        if (objValueOf != null && string != null && string2 != null && string2.startsWith(string)) {
                                            i2 = 0;
                                            try {
                                                arrayList.add(0, albumEntry5);
                                                objValueOf = Integer.valueOf(i5);
                                            } catch (Throwable th17) {
                                                th = th17;
                                                albumEntry8 = albumEntry3;
                                                albumEntry2 = albumEntry4;
                                                FileLog.e(th);
                                                if (cursorQuery != null) {
                                                    cursorQuery.close();
                                                }
                                                AlbumEntry albumEntry111 = albumEntry8;
                                                AlbumEntry albumEntry112 = albumEntry2;
                                                Integer num6 = objValueOf;
                                                while (i3 < arrayList.size()) {
                                                    Collections.sort(((AlbumEntry) arrayList.get(i3)).photos, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda42
                                                        @Override // java.util.Comparator
                                                        public final int compare(Object obj, Object obj2) {
                                                            return MediaController.m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE((MediaController.PhotoEntry) obj, (MediaController.PhotoEntry) obj2);
                                                        }
                                                    });
                                                }
                                                broadcastNewPhotos(i, arrayList, arrayList2, num6, albumEntry112, albumEntry, albumEntry111, 0);
                                            }
                                        } else {
                                            arrayList.add(albumEntry5);
                                        }
                                    }
                                    albumEntry5.addPhoto(photoEntry);
                                    albumEntry8 = albumEntry3;
                                    albumEntry2 = albumEntry4;
                                    columnIndex5 = i117;
                                    columnIndex = i119;
                                    columnIndex2 = columnIndex2;
                                    columnIndex9 = i118;
                                    columnIndex4 = columnIndex4;
                                    columnIndex3 = columnIndex3;
                                    columnIndex6 = columnIndex6;
                                }
                            }
                        }
                    }
                    i2 = 0;
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    AlbumEntry albumEntry113 = albumEntry8;
                    AlbumEntry albumEntry114 = albumEntry2;
                    Integer num7 = objValueOf;
                    while (i3 < arrayList.size()) {
                        Collections.sort(((AlbumEntry) arrayList.get(i3)).photos, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda42
                            @Override // java.util.Comparator
                            public final int compare(Object obj, Object obj2) {
                                return MediaController.m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE((MediaController.PhotoEntry) obj, (MediaController.PhotoEntry) obj2);
                            }
                        });
                    }
                    broadcastNewPhotos(i, arrayList, arrayList2, num7, albumEntry114, albumEntry, albumEntry113, 0);
                }
                cursorQuery = null;
                albumEntry = null;
                albumEntry2 = albumEntry;
                objValueOf = albumEntry2;
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (Throwable th18) {
                th = th18;
                str4 = "height";
                str5 = "width";
                str6 = "orientation";
                str7 = "_data";
                str8 = "bucket_display_name";
                str9 = "bucket_id";
                str10 = "_id";
                cursorQuery = null;
                albumEntry = null;
            }
            i4 = Build.VERSION.SDK_INT;
            if (SystemUtils.isVideoPermissionGranted()) {
                ContentResolver contentResolver6 = ApplicationLoader.applicationContext.getContentResolver();
                Uri uri6 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] strArr6 = projectionVideo;
                StringBuilder sb7 = new StringBuilder();
                if (i4 > 28) {
                    str2 = "date_modified";
                } else {
                    str2 = str;
                }
                sb7.append(str2);
                sb7.append(" DESC");
                cursorQuery = MediaStore.Images.Media.query(contentResolver6, uri6, strArr6, null, null, sb7.toString());
                if (cursorQuery != null) {
                    columnIndex = cursorQuery.getColumnIndex(str10);
                    columnIndex2 = cursorQuery.getColumnIndex(str9);
                    columnIndex3 = cursorQuery.getColumnIndex(str8);
                    columnIndex4 = cursorQuery.getColumnIndex(str7);
                    if (i4 > 28) {
                        str3 = "date_modified";
                    } else {
                        str3 = str;
                    }
                    columnIndex5 = cursorQuery.getColumnIndex(str3);
                    columnIndex6 = cursorQuery.getColumnIndex("duration");
                    columnIndex7 = cursorQuery.getColumnIndex(str5);
                    columnIndex8 = cursorQuery.getColumnIndex(str4);
                    columnIndex9 = cursorQuery.getColumnIndex("_size");
                    cursorQuery.getColumnIndex(str6);
                    while (cursorQuery.moveToNext()) {
                        string2 = cursorQuery.getString(columnIndex4);
                        if (!TextUtils.isEmpty(string2)) {
                            int i1110 = cursorQuery.getInt(columnIndex);
                            i5 = cursorQuery.getInt(columnIndex2);
                            string3 = cursorQuery.getString(columnIndex3);
                            int i1111 = columnIndex5;
                            int i1112 = columnIndex9;
                            photoEntry = new PhotoEntry(i5, i1110, cursorQuery.getLong(columnIndex5), string2, 0, (int) (cursorQuery.getLong(columnIndex6) / 1000), true, cursorQuery.getInt(columnIndex7), cursorQuery.getInt(columnIndex8), cursorQuery.getLong(columnIndex9));
                            int i1113 = columnIndex;
                            if (albumEntry8 == null) {
                                albumEntry3 = new AlbumEntry(0, LocaleController.getString(R.string.AllVideos), photoEntry);
                                i6 = 1;
                                albumEntry3.videoOnly = true;
                                if (albumEntry2 == null) {
                                    i6 = 0;
                                }
                                if (albumEntry != null) {
                                    i6++;
                                }
                                arrayList.add(i6, albumEntry3);
                            } else {
                                albumEntry3 = albumEntry8;
                            }
                            if (albumEntry2 == null) {
                                albumEntry4 = new AlbumEntry(0, LocaleController.getString(R.string.AllMedia), photoEntry);
                                arrayList.add(0, albumEntry4);
                            } else {
                                albumEntry4 = albumEntry2;
                            }
                            albumEntry3.addPhoto(photoEntry);
                            albumEntry4.addPhoto(photoEntry);
                            albumEntry5 = (AlbumEntry) sparseArray.get(i5);
                            if (albumEntry5 == null) {
                                albumEntry5 = new AlbumEntry(i5, string3, photoEntry);
                                sparseArray.put(i5, albumEntry5);
                                if (objValueOf != null) {
                                    arrayList.add(albumEntry5);
                                } else {
                                    arrayList.add(albumEntry5);
                                }
                            }
                            albumEntry5.addPhoto(photoEntry);
                            albumEntry8 = albumEntry3;
                            albumEntry2 = albumEntry4;
                            columnIndex5 = i1111;
                            columnIndex = i1113;
                            columnIndex2 = columnIndex2;
                            columnIndex9 = i1112;
                            columnIndex4 = columnIndex4;
                            columnIndex3 = columnIndex3;
                            columnIndex6 = columnIndex6;
                        }
                    }
                }
            }
            i2 = 0;
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Throwable th19) {
            th = th19;
        }
        AlbumEntry albumEntry115 = albumEntry8;
        AlbumEntry albumEntry116 = albumEntry2;
        Integer num8 = objValueOf;
        while (i3 < arrayList.size()) {
            Collections.sort(((AlbumEntry) arrayList.get(i3)).photos, new Comparator() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda42
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return MediaController.m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE((MediaController.PhotoEntry) obj, (MediaController.PhotoEntry) obj2);
                }
            });
        }
        broadcastNewPhotos(i, arrayList, arrayList2, num8, albumEntry116, albumEntry, albumEntry115, 0);
    }

    /* JADX INFO: renamed from: $r8$lambda$GAH4--v5NoM1OoDPvbTdDq1svaE, reason: not valid java name */
    public static /* synthetic */ int m3133$r8$lambda$GAH4v5NoM1OoDPvbTdDq1svaE(PhotoEntry photoEntry, PhotoEntry photoEntry2) {
        long j = photoEntry.dateTaken;
        long j2 = photoEntry2.dateTaken;
        if (j < j2) {
            return 1;
        }
        return j > j2 ? -1 : 0;
    }

    private static void broadcastNewPhotos(final int i, final ArrayList<AlbumEntry> arrayList, final ArrayList<AlbumEntry> arrayList2, final Integer num, final AlbumEntry albumEntry, final AlbumEntry albumEntry2, final AlbumEntry albumEntry3, int i2) {
        Runnable runnable = broadcastPhotosRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.m3125$r8$lambda$83dDT6Q5pok_4JcwnJcME2k4Lk(i, arrayList, arrayList2, num, albumEntry, albumEntry2, albumEntry3);
            }
        };
        broadcastPhotosRunnable = runnable2;
        AndroidUtilities.runOnUIThread(runnable2, i2);
    }

    /* JADX INFO: renamed from: $r8$lambda$-83dDT6Q5pok_4JcwnJcME2k4Lk, reason: not valid java name */
    public static /* synthetic */ void m3125$r8$lambda$83dDT6Q5pok_4JcwnJcME2k4Lk(int i, ArrayList arrayList, ArrayList arrayList2, Integer num, AlbumEntry albumEntry, AlbumEntry albumEntry2, AlbumEntry albumEntry3) {
        if (PhotoViewer.getInstance().isVisible() && !forceBroadcastNewPhotos) {
            broadcastNewPhotos(i, arrayList, arrayList2, num, albumEntry, albumEntry2, albumEntry3, MediaDataController.MAX_STYLE_RUNS_COUNT);
            return;
        }
        allMediaAlbums = arrayList;
        allPhotoAlbums = arrayList2;
        broadcastPhotosRunnable = null;
        allPhotosAlbumEntry = albumEntry2;
        allMediaAlbumEntry = albumEntry;
        allVideosAlbumEntry = albumEntry3;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.albumsDidLoad, Integer.valueOf(i), arrayList, arrayList2, num);
    }

    public void scheduleVideoConvert(MessageObject messageObject) {
        scheduleVideoConvert(messageObject, false, true, false);
    }

    public boolean scheduleVideoConvert(MessageObject messageObject, boolean z, boolean z2, boolean z3) {
        if (messageObject == null || messageObject.videoEditedInfo == null) {
            return false;
        }
        if (z && !this.videoConvertQueue.isEmpty()) {
            return false;
        }
        if (z) {
            new File(messageObject.messageOwner.attachPath).delete();
        }
        VideoConvertMessage videoConvertMessage = new VideoConvertMessage(messageObject, messageObject.videoEditedInfo, z2, z3);
        this.videoConvertQueue.add(videoConvertMessage);
        if (videoConvertMessage.foreground) {
            this.foregroundConvertingMessages.add(videoConvertMessage);
            checkForegroundConvertMessage(false);
        }
        if (this.videoConvertQueue.size() == 1) {
            startVideoConvertFromQueue();
        }
        return true;
    }

    public void cancelVideoConvert(MessageObject messageObject) {
        if (messageObject == null || this.videoConvertQueue.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.videoConvertQueue.size(); i++) {
            VideoConvertMessage videoConvertMessage = this.videoConvertQueue.get(i);
            MessageObject messageObject2 = videoConvertMessage.messageObject;
            if (messageObject2.equals(messageObject) && messageObject2.currentAccount == messageObject.currentAccount) {
                if (i == 0) {
                    synchronized (this.videoConvertSync) {
                        videoConvertMessage.videoEditedInfo.canceled = true;
                    }
                    return;
                } else {
                    this.foregroundConvertingMessages.remove(this.videoConvertQueue.remove(i));
                    checkForegroundConvertMessage(true);
                    return;
                }
            }
        }
    }

    private void checkForegroundConvertMessage(boolean z) {
        if (!this.foregroundConvertingMessages.isEmpty()) {
            this.currentForegroundConvertingVideo = this.foregroundConvertingMessages.get(0);
        } else {
            this.currentForegroundConvertingVideo = null;
        }
        if (this.currentForegroundConvertingVideo != null || z) {
            VideoEncodingService.start(z);
        }
    }

    private boolean startVideoConvertFromQueue() {
        if (this.videoConvertQueue.isEmpty()) {
            return false;
        }
        VideoConvertMessage videoConvertMessage = this.videoConvertQueue.get(0);
        VideoEditedInfo videoEditedInfo = videoConvertMessage.videoEditedInfo;
        synchronized (this.videoConvertSync) {
            if (videoEditedInfo != null) {
                try {
                    videoEditedInfo.canceled = false;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        VideoConvertRunnable.runConversion(videoConvertMessage);
        return true;
    }

    @SuppressLint({"NewApi"})
    public static MediaCodecInfo selectCodec(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        MediaCodecInfo mediaCodecInfo = null;
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        String name = codecInfoAt.getName();
                        if (name != null && (!name.equals("OMX.SEC.avc.enc") || name.equals("OMX.SEC.AVC.Encoder"))) {
                            return codecInfoAt;
                        }
                        mediaCodecInfo = codecInfoAt;
                    }
                }
            }
        }
        return mediaCodecInfo;
    }

    @SuppressLint({"NewApi"})
    public static int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
        int i = 0;
        int i2 = 0;
        while (true) {
            int[] iArr = capabilitiesForType.colorFormats;
            if (i >= iArr.length) {
                return i2;
            }
            int i3 = iArr[i];
            if (isRecognizedFormat(i3)) {
                if (!mediaCodecInfo.getName().equals("OMX.SEC.AVC.Encoder") || i3 != 19) {
                    return i3;
                }
                i2 = i3;
            }
            i++;
        }
    }

    public static int findTrack(MediaExtractor mediaExtractor, boolean z) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            String string = mediaExtractor.getTrackFormat(i).getString("mime");
            if (z) {
                if (string.startsWith("audio/")) {
                    return i;
                }
            } else {
                if (string.startsWith("video/")) {
                    return i;
                }
            }
        }
        return -5;
    }

    public static boolean isH264Video(String str) {
        MediaExtractor mediaExtractor = new MediaExtractor();
        boolean z = false;
        try {
            try {
                mediaExtractor.setDataSource(str);
                int iFindTrack = findTrack(mediaExtractor, false);
                if (iFindTrack >= 0 && mediaExtractor.getTrackFormat(iFindTrack).getString("mime").equals(VIDEO_MIME_TYPE)) {
                    z = true;
                }
                return z;
            } catch (Exception e) {
                FileLog.e(e);
                return false;
            }
        } finally {
            mediaExtractor.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void didWriteData(final VideoConvertMessage videoConvertMessage, final File file, final boolean z, final long j, final long j2, final boolean z2, final float f) {
        VideoEditedInfo videoEditedInfo = videoConvertMessage.videoEditedInfo;
        final boolean z3 = videoEditedInfo.videoConvertFirstWrite;
        if (z3) {
            videoEditedInfo.videoConvertFirstWrite = false;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$didWriteData$54(z2, z, videoConvertMessage, file, f, j, z3, j2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didWriteData$54(boolean z, boolean z2, VideoConvertMessage videoConvertMessage, File file, float f, long j, boolean z3, long j2) {
        if (z || z2) {
            boolean z4 = videoConvertMessage.videoEditedInfo.canceled;
            synchronized (this.videoConvertSync) {
                videoConvertMessage.videoEditedInfo.canceled = false;
            }
            this.videoConvertQueue.remove(videoConvertMessage);
            this.foregroundConvertingMessages.remove(videoConvertMessage);
            checkForegroundConvertMessage(z4 || z);
            startVideoConvertFromQueue();
        }
        if (z) {
            NotificationCenter.getInstance(videoConvertMessage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.filePreparingFailed, videoConvertMessage.messageObject, file.toString(), Float.valueOf(f), Long.valueOf(j));
            return;
        }
        if (z3) {
            NotificationCenter.getInstance(videoConvertMessage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.filePreparingStarted, videoConvertMessage.messageObject, file.toString(), Float.valueOf(f), Long.valueOf(j));
        }
        NotificationCenter.getInstance(videoConvertMessage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileNewChunkAvailable, videoConvertMessage.messageObject, file.toString(), Long.valueOf(j2), Long.valueOf(z2 ? file.length() : 0L), Float.valueOf(f), Long.valueOf(j));
    }

    public void pauseByRewind() {
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer != null) {
            videoPlayer.pause();
        }
    }

    public void resumeByRewind() {
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer == null || this.playingMessageObject == null || this.isPaused) {
            return;
        }
        if (videoPlayer.isBuffering()) {
            MessageObject messageObject = this.playingMessageObject;
            cleanupPlayer(false, false);
            playMessage(messageObject);
            return;
        }
        this.audioPlayer.play();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class VideoConvertRunnable implements Runnable {
        private VideoConvertMessage convertMessage;

        private VideoConvertRunnable(VideoConvertMessage videoConvertMessage) {
            this.convertMessage = videoConvertMessage;
        }

        @Override // java.lang.Runnable
        public void run() {
            MediaController.getInstance().convertVideo(this.convertMessage);
        }

        public static void runConversion(final VideoConvertMessage videoConvertMessage) {
            new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$VideoConvertRunnable$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.VideoConvertRunnable.$r8$lambda$Crg9xK2aVHTQWtPt_7wiWv_ZWpY(videoConvertMessage);
                }
            }).start();
        }

        public static /* synthetic */ void $r8$lambda$Crg9xK2aVHTQWtPt_7wiWv_ZWpY(VideoConvertMessage videoConvertMessage) {
            try {
                Thread thread = new Thread(new VideoConvertRunnable(videoConvertMessage), "VideoConvertRunnable");
                thread.start();
                thread.join();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean convertVideo(final VideoConvertMessage videoConvertMessage) {
        int i;
        int i2;
        long j;
        int i3;
        MessageObject messageObject = videoConvertMessage.messageObject;
        final VideoEditedInfo videoEditedInfo = videoConvertMessage.videoEditedInfo;
        if (messageObject == null || videoEditedInfo == null) {
            return false;
        }
        String str = videoEditedInfo.originalPath;
        long j2 = videoEditedInfo.startTime;
        long j3 = videoEditedInfo.avatarStartTime;
        long j4 = videoEditedInfo.endTime;
        int i4 = videoEditedInfo.resultWidth;
        int i5 = videoEditedInfo.resultHeight;
        int i6 = videoEditedInfo.rotationValue;
        int i7 = videoEditedInfo.originalWidth;
        int i8 = videoEditedInfo.originalHeight;
        int i9 = videoEditedInfo.framerate;
        int i10 = videoEditedInfo.bitrate;
        int i11 = videoEditedInfo.originalBitrate;
        boolean z = DialogObject.isEncryptedDialog(messageObject.getDialogId()) || videoEditedInfo.forceFragmenting;
        final File file = new File(messageObject.messageOwner.attachPath);
        if (file.exists()) {
            file.delete();
        }
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("begin convert ");
            sb.append(str);
            sb.append(" startTime = ");
            sb.append(j2);
            sb.append(" avatarStartTime = ");
            sb.append(j3);
            sb.append(" endTime ");
            sb.append(j4);
            sb.append(" rWidth = ");
            sb.append(i4);
            sb.append(" rHeight = ");
            sb.append(i5);
            sb.append(" rotation = ");
            sb.append(i6);
            sb.append(" oWidth = ");
            sb.append(i7);
            sb.append(" oHeight = ");
            sb.append(i8);
            sb.append(" framerate = ");
            sb.append(i9);
            sb.append(" bitrate = ");
            sb.append(i);
            sb.append(" originalBitrate = ");
            i2 = i11;
            sb.append(i2);
            FileLog.d(sb.toString());
        } else {
            i2 = i11;
        }
        if (str == null) {
            i = i10;
            i = i10;
            str = _UrlKt.FRAGMENT_ENCODE_SET;
        }
        i = i10;
        i = i10;
        if (j2 > 0 && j4 > 0) {
            j = j4 - j2;
        } else if (j4 > 0) {
            j = j4;
        } else if (j2 > 0) {
            j = videoEditedInfo.originalDuration - j2;
        } else {
            j = videoEditedInfo.originalDuration;
        }
        int i12 = i9 == 0 ? 25 : i9;
        if (i6 == 90 || i6 == 270) {
            i3 = i5;
        } else {
            i3 = i4;
            i4 = i5;
        }
        if (!videoEditedInfo.shouldLimitFps && i12 > 40 && Math.min(i4, i3) <= 480) {
            i12 = 30;
        }
        int i13 = i12;
        boolean z2 = (j3 == -1 && videoEditedInfo.cropState == null && videoEditedInfo.mediaEntities == null && videoEditedInfo.paintPath == null && videoEditedInfo.filterState == null && i3 == i7 && i4 == i8 && i6 == 0 && !videoEditedInfo.roundVideo && j2 == -1 && videoEditedInfo.mixedSoundInfos.isEmpty()) ? false : true;
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("videoconvert", 0);
        long jCurrentTimeMillis = System.currentTimeMillis();
        VideoConvertorListener videoConvertorListener = new VideoConvertorListener() { // from class: org.telegram.messenger.MediaController.18
            private long lastAvailableSize = 0;

            @Override // org.telegram.messenger.MediaController.VideoConvertorListener
            public boolean checkConversionCanceled() {
                return videoEditedInfo.canceled;
            }

            @Override // org.telegram.messenger.MediaController.VideoConvertorListener
            public void didWriteData(long j5, float f) {
                if (videoEditedInfo.canceled) {
                    return;
                }
                if (j5 < 0) {
                    j5 = file.length();
                }
                long j6 = j5;
                if (videoEditedInfo.needUpdateProgress || this.lastAvailableSize != j6) {
                    this.lastAvailableSize = j6;
                    MediaController.this.didWriteData(videoConvertMessage, file, false, 0L, j6, false, f);
                }
            }
        };
        videoEditedInfo.videoConvertFirstWrite = true;
        MediaCodecVideoConvertor mediaCodecVideoConvertor = new MediaCodecVideoConvertor();
        MediaCodecVideoConvertor.ConvertVideoParams convertVideoParamsOf = MediaCodecVideoConvertor.ConvertVideoParams.of(str, file, i6, z, i7, i8, i3, i4, i13, i, i2, j2, j4, j3, z2, j, videoConvertorListener, videoEditedInfo);
        convertVideoParamsOf.soundInfos.addAll(videoEditedInfo.mixedSoundInfos);
        boolean zConvertVideo = mediaCodecVideoConvertor.convertVideo(convertVideoParamsOf);
        boolean z3 = videoEditedInfo.canceled;
        if (!z3) {
            synchronized (this.videoConvertSync) {
                z3 = videoEditedInfo.canceled;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("time=" + (System.currentTimeMillis() - jCurrentTimeMillis) + " canceled=" + z3);
        }
        sharedPreferences.edit().putBoolean("isPreviousOk", true).apply();
        didWriteData(videoConvertMessage, file, true, mediaCodecVideoConvertor.getLastFrameTimestamp(), file.length(), zConvertVideo || z3, 1.0f);
        return true;
    }

    public static int getVideoBitrate(String str) {
        int i;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            i = Integer.parseInt(mediaMetadataRetriever.extractMetadata(20));
        } catch (Exception e) {
            FileLog.e(e);
            i = 0;
        }
        try {
            mediaMetadataRetriever.release();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return i;
    }

    public static int makeVideoBitrate(int i, int i2, int i3, int i4, int i5) {
        int i6;
        if (Math.min(i4, i5) >= 2160) {
            i6 = 62000000;
        } else if (Math.min(i4, i5) >= 1440) {
            i6 = 24000000;
        } else if (Math.min(i4, i5) >= 1080) {
            i6 = 12000000;
        } else if (Math.min(i4, i5) >= 720) {
            i6 = 7500000;
        } else if (Math.min(i4, i5) >= 480) {
            i6 = 4000000;
        } else {
            i6 = Math.min(i4, i5) >= 360 ? 1500000 : 1000000;
        }
        int iMin = (int) (i3 / Math.min(i / i4, i2 / i5));
        int videoBitrateWithFactor = (int) (getVideoBitrateWithFactor(1.0f) / (921600.0f / (i5 * i4)));
        if (i3 < videoBitrateWithFactor) {
            return iMin;
        }
        return iMin > i6 ? i6 : Math.max(iMin, videoBitrateWithFactor);
    }

    public static int extractRealEncoderBitrate(int i, int i2, int i3, boolean z) {
        MediaCodec mediaCodecCreateEncoderByType;
        String str = i + _UrlKt.FRAGMENT_ENCODE_SET + i2 + _UrlKt.FRAGMENT_ENCODE_SET + i3;
        Integer num = cachedEncoderBitrates.get(str);
        if (num != null) {
            return num.intValue();
        }
        if (z) {
            try {
                mediaCodecCreateEncoderByType = MediaCodec.createEncoderByType("video/hevc");
            } catch (Exception unused) {
                mediaCodecCreateEncoderByType = null;
            }
        } else {
            mediaCodecCreateEncoderByType = null;
        }
        if (mediaCodecCreateEncoderByType == null) {
            try {
                mediaCodecCreateEncoderByType = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
            } catch (Exception unused2) {
                return i3;
            }
        }
        MediaFormat mediaFormatCreateVideoFormat = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, i, i2);
        mediaFormatCreateVideoFormat.setInteger("color-format", 2130708361);
        mediaFormatCreateVideoFormat.setInteger("max-bitrate", i3);
        mediaFormatCreateVideoFormat.setInteger("bitrate", i3);
        mediaFormatCreateVideoFormat.setInteger("frame-rate", 30);
        mediaFormatCreateVideoFormat.setInteger("i-frame-interval", 1);
        mediaCodecCreateEncoderByType.configure(mediaFormatCreateVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        int integer = mediaCodecCreateEncoderByType.getOutputFormat().getInteger("bitrate");
        cachedEncoderBitrates.put(str, Integer.valueOf(integer));
        mediaCodecCreateEncoderByType.release();
        return integer;
    }

    public static class PlaylistGlobalSearchParams {
        final long dialogId;
        public boolean endReached;
        final FiltersView.MediaFilterData filter;
        public int folderId;
        final long maxDate;
        final long minDate;
        public int nextSearchRate;
        final String query;
        public ReactionsLayoutInBubble.VisibleReaction reaction;
        public long topicId;
        public int totalCount;

        public PlaylistGlobalSearchParams(String str, long j, long j2, long j3, FiltersView.MediaFilterData mediaFilterData) {
            this.filter = mediaFilterData;
            this.query = str;
            this.dialogId = j;
            this.minDate = j2;
            this.maxDate = j3;
        }
    }

    public boolean currentPlaylistIsGlobalSearch() {
        return this.playlistGlobalSearchParams != null;
    }

    private static class SavedMusicPlaylistState {
        public final MessageObject playingMessage;
        public final float progress;
        public final int progressMs;
        public final int progressSec;

        public SavedMusicPlaylistState(MessageObject messageObject) {
            this.playingMessage = messageObject;
            this.progress = messageObject.audioProgress;
            this.progressMs = messageObject.audioProgressMs;
            this.progressSec = messageObject.audioProgressSec;
        }
    }

    private void clearMusicPlaylistState() {
        this.savedMusicPlaylistState = null;
    }

    private boolean saveMusicPlaylistStateIfNeeded() {
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject == null || !messageObject.isMusic() || this.playlist.isEmpty()) {
            return this.savedMusicPlaylistState != null;
        }
        this.savedMusicPlaylistState = new SavedMusicPlaylistState(this.playingMessageObject);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean restoreMusicPlaylistState() {
        int i;
        MessageObject messageObject;
        SavedMusicPlaylistState savedMusicPlaylistState = this.savedMusicPlaylistState;
        if (savedMusicPlaylistState == null) {
            return false;
        }
        this.savedMusicPlaylistState = null;
        ArrayList<MessageObject> arrayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
        if (arrayList == null || (i = this.currentPlaylistNum) < 0 || i >= arrayList.size() || (messageObject = arrayList.get(this.currentPlaylistNum)) == null || messageObject.getDialogId() != savedMusicPlaylistState.playingMessage.getDialogId() || messageObject.getId() != savedMusicPlaylistState.playingMessage.getId()) {
            return false;
        }
        this.playMusicAgain = false;
        float f = savedMusicPlaylistState.progress;
        messageObject.forceSeekTo = f;
        messageObject.audioProgress = f;
        messageObject.audioProgressMs = savedMusicPlaylistState.progressMs;
        messageObject.audioProgressSec = savedMusicPlaylistState.progressSec;
        playMessage(messageObject);
        pauseMessage(messageObject, false);
        return true;
    }
}
