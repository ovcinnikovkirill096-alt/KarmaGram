package androidx.media;

import android.media.session.MediaSessionManager;

final class MediaSessionManagerImplApi28$RemoteUserInfoImplApi28 extends MediaSessionManagerImplBase$RemoteUserInfoImplBase {
    final MediaSessionManager.RemoteUserInfo mObject;

    MediaSessionManagerImplApi28$RemoteUserInfoImplApi28(String str, int i, int i2) {
        super(str, i, i2);
        this.mObject = MediaSessionManagerImplApi28$RemoteUserInfoImplApi28$$ExternalSyntheticApiModelOutline0.m(str, i, i2);
    }

    MediaSessionManagerImplApi28$RemoteUserInfoImplApi28(MediaSessionManager.RemoteUserInfo remoteUserInfo) {
        super(remoteUserInfo.getPackageName(), remoteUserInfo.getPid(), remoteUserInfo.getUid());
        this.mObject = remoteUserInfo;
    }

    static String getPackageName(MediaSessionManager.RemoteUserInfo remoteUserInfo) {
        return remoteUserInfo.getPackageName();
    }
}
