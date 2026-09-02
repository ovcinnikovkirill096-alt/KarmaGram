package androidx.mediarouter.media;

import android.content.Context;
import android.media.RemoteControlClient;
import java.lang.ref.WeakReference;
import okhttp3.internal.url._UrlKt;

abstract class RemoteControlClientCompat {
    protected final Context mContext;
    protected final RemoteControlClient mRcc;
    protected VolumeCallback mVolumeCallback;

    public static final class PlaybackInfo {
        public int volume;
        public String volumeControlId;
        public int volumeMax;
        public int volumeHandling = 0;
        public int playbackStream = 3;
        public int playbackType = 1;
    }

    public interface VolumeCallback {
        void onVolumeSetRequest(int i);

        void onVolumeUpdateRequest(int i);
    }

    public abstract void setPlaybackInfo(PlaybackInfo playbackInfo);

    protected RemoteControlClientCompat(Context context, RemoteControlClient remoteControlClient) {
        this.mContext = context;
        this.mRcc = remoteControlClient;
    }

    public static RemoteControlClientCompat obtain(Context context, RemoteControlClient remoteControlClient) {
        return new JellybeanImpl(context, remoteControlClient);
    }

    public RemoteControlClient getRemoteControlClient() {
        return this.mRcc;
    }

    public void setVolumeCallback(VolumeCallback volumeCallback) {
        this.mVolumeCallback = volumeCallback;
    }

    static class JellybeanImpl extends RemoteControlClientCompat {
        private boolean mRegistered;
        private final android.media.MediaRouter mRouter;
        private final android.media.MediaRouter.UserRouteInfo mUserRoute;
        private final android.media.MediaRouter.RouteCategory mUserRouteCategory;

        JellybeanImpl(Context context, RemoteControlClient remoteControlClient) {
            super(context, remoteControlClient);
            android.media.MediaRouter mediaRouter = (android.media.MediaRouter) context.getSystemService("media_router");
            this.mRouter = mediaRouter;
            android.media.MediaRouter.RouteCategory routeCategoryCreateRouteCategory = mediaRouter.createRouteCategory((CharSequence) _UrlKt.FRAGMENT_ENCODE_SET, false);
            this.mUserRouteCategory = routeCategoryCreateRouteCategory;
            this.mUserRoute = mediaRouter.createUserRoute(routeCategoryCreateRouteCategory);
        }

        @Override // androidx.mediarouter.media.RemoteControlClientCompat
        public void setPlaybackInfo(PlaybackInfo playbackInfo) {
            this.mUserRoute.setVolume(playbackInfo.volume);
            this.mUserRoute.setVolumeMax(playbackInfo.volumeMax);
            this.mUserRoute.setVolumeHandling(playbackInfo.volumeHandling);
            this.mUserRoute.setPlaybackStream(playbackInfo.playbackStream);
            this.mUserRoute.setPlaybackType(playbackInfo.playbackType);
            if (this.mRegistered) {
                return;
            }
            this.mRegistered = true;
            this.mUserRoute.setVolumeCallback(MediaRouterUtils.createVolumeCallback(new VolumeCallbackWrapper(this)));
            this.mUserRoute.setRemoteControlClient(this.mRcc);
        }

        private static final class VolumeCallbackWrapper implements MediaRouterUtils.VolumeCallback {
            private final WeakReference mImplWeak;

            public VolumeCallbackWrapper(JellybeanImpl jellybeanImpl) {
                this.mImplWeak = new WeakReference(jellybeanImpl);
            }

            @Override // androidx.mediarouter.media.MediaRouterUtils.VolumeCallback
            public void onVolumeUpdateRequest(android.media.MediaRouter.RouteInfo routeInfo, int i) {
                VolumeCallback volumeCallback;
                JellybeanImpl jellybeanImpl = (JellybeanImpl) this.mImplWeak.get();
                if (jellybeanImpl == null || (volumeCallback = jellybeanImpl.mVolumeCallback) == null) {
                    return;
                }
                volumeCallback.onVolumeUpdateRequest(i);
            }

            @Override // androidx.mediarouter.media.MediaRouterUtils.VolumeCallback
            public void onVolumeSetRequest(android.media.MediaRouter.RouteInfo routeInfo, int i) {
                VolumeCallback volumeCallback;
                JellybeanImpl jellybeanImpl = (JellybeanImpl) this.mImplWeak.get();
                if (jellybeanImpl == null || (volumeCallback = jellybeanImpl.mVolumeCallback) == null) {
                    return;
                }
                volumeCallback.onVolumeSetRequest(i);
            }
        }
    }
}
