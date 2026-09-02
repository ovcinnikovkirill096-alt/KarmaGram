package com.radolyn.ayugram.utils.android;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import com.exteragram.messenger.badges.BadgesController;
import com.exteragram.messenger.nowplaying.ui.components.NowPlayingCard$$ExternalSyntheticApiModelOutline0;
import org.telegram.messenger.UserConfig;
import org.webrtc.MediaStreamTrack;

public class AudioOutputManager {
    private AudioFocusRequest audioFocusRequest;
    private final AudioManager audioManager;
    private int maxVolume;
    private final float modifier;
    private int originalMode;
    private boolean originalSpeakerphoneOn;
    private int originalVolume;

    /* JADX INFO: renamed from: $r8$lambda$S2z2DZZP2QzdiNKo_KSZDQ0SI-w, reason: not valid java name */
    public static /* synthetic */ void m2404$r8$lambda$S2z2DZZP2QzdiNKo_KSZDQ0SIw(int i) {
    }

    public AudioOutputManager(Context context) {
        this.audioManager = (AudioManager) context.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        float f = 0.7f;
        try {
            BadgesController badgesController = BadgesController.INSTANCE;
            boolean zIsDeveloper = badgesController.isDeveloper(UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser());
            if (zIsDeveloper ? zIsDeveloper : badgesController.isAyuModerator(UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser())) {
                f = 0.1f;
            }
        } catch (Exception unused) {
        }
        this.modifier = f;
    }

    public void increaseVolume() {
        try {
            this.audioManager.setStreamVolume(3, this.maxVolume, 0);
        } catch (Exception unused) {
        }
    }

    public void decreaseVolume() {
        try {
            this.audioManager.setStreamVolume(3, this.originalVolume, 0);
        } catch (Exception unused) {
        }
    }

    public void switchToSpeaker() {
        this.originalMode = this.audioManager.getMode();
        this.originalSpeakerphoneOn = this.audioManager.isSpeakerphoneOn();
        this.originalVolume = this.audioManager.getStreamVolume(3);
        this.maxVolume = (int) (this.audioManager.getStreamMaxVolume(3) * this.modifier);
        this.audioManager.setMode(3);
        this.audioManager.setSpeakerphoneOn(true);
    }

    public void revertAudioOutput() {
        this.audioManager.setMode(this.originalMode);
        this.audioManager.setSpeakerphoneOn(this.originalSpeakerphoneOn);
    }

    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= 26) {
            AudioFocusRequest audioFocusRequestBuild = NowPlayingCard$$ExternalSyntheticApiModelOutline0.m(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(1).setContentType(2).build()).setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() { // from class: com.radolyn.ayugram.utils.android.AudioOutputManager$$ExternalSyntheticLambda0
                @Override // android.media.AudioManager.OnAudioFocusChangeListener
                public final void onAudioFocusChange(int i) {
                    AudioOutputManager.m2404$r8$lambda$S2z2DZZP2QzdiNKo_KSZDQ0SIw(i);
                }
            }).build();
            this.audioFocusRequest = audioFocusRequestBuild;
            this.audioManager.requestAudioFocus(audioFocusRequestBuild);
            return;
        }
        this.audioManager.requestAudioFocus(null, 3, 1);
    }

    public void beforePlay() {
        requestAudioFocus();
        switchToSpeaker();
    }

    public void afterPlay() {
        revertAudioOutput();
    }
}
