package androidx.mediarouter.media;

abstract class MediaRouterUtils {

    public interface Callback {
        void onRouteAdded(android.media.MediaRouter.RouteInfo routeInfo);

        void onRouteChanged(android.media.MediaRouter.RouteInfo routeInfo);

        void onRouteGrouped(android.media.MediaRouter.RouteInfo routeInfo, android.media.MediaRouter.RouteGroup routeGroup, int i);

        void onRoutePresentationDisplayChanged(android.media.MediaRouter.RouteInfo routeInfo);

        void onRouteRemoved(android.media.MediaRouter.RouteInfo routeInfo);

        void onRouteSelected(int i, android.media.MediaRouter.RouteInfo routeInfo);

        void onRouteUngrouped(android.media.MediaRouter.RouteInfo routeInfo, android.media.MediaRouter.RouteGroup routeGroup);

        void onRouteUnselected(int i, android.media.MediaRouter.RouteInfo routeInfo);

        void onRouteVolumeChanged(android.media.MediaRouter.RouteInfo routeInfo);
    }

    public interface VolumeCallback {
        void onVolumeSetRequest(android.media.MediaRouter.RouteInfo routeInfo, int i);

        void onVolumeUpdateRequest(android.media.MediaRouter.RouteInfo routeInfo, int i);
    }

    public static android.media.MediaRouter.Callback createCallback(Callback callback) {
        return new CallbackProxy(callback);
    }

    public static android.media.MediaRouter.VolumeCallback createVolumeCallback(VolumeCallback volumeCallback) {
        return new VolumeCallbackProxy(volumeCallback);
    }

    static class CallbackProxy extends android.media.MediaRouter.Callback {
        protected final Callback mCallback;

        CallbackProxy(Callback callback) {
            this.mCallback = callback;
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteSelected(android.media.MediaRouter mediaRouter, int i, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteSelected(i, routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteUnselected(android.media.MediaRouter mediaRouter, int i, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteUnselected(i, routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteAdded(android.media.MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteAdded(routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteRemoved(android.media.MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteRemoved(routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteChanged(android.media.MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteChanged(routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteGrouped(android.media.MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo, android.media.MediaRouter.RouteGroup routeGroup, int i) {
            this.mCallback.onRouteGrouped(routeInfo, routeGroup, i);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteUngrouped(android.media.MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo, android.media.MediaRouter.RouteGroup routeGroup) {
            this.mCallback.onRouteUngrouped(routeInfo, routeGroup);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteVolumeChanged(android.media.MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteVolumeChanged(routeInfo);
        }

        @Override // android.media.MediaRouter.Callback
        public void onRoutePresentationDisplayChanged(android.media.MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRoutePresentationDisplayChanged(routeInfo);
        }
    }

    static class VolumeCallbackProxy extends android.media.MediaRouter.VolumeCallback {
        protected final VolumeCallback mCallback;

        VolumeCallbackProxy(VolumeCallback volumeCallback) {
            this.mCallback = volumeCallback;
        }

        @Override // android.media.MediaRouter.VolumeCallback
        public void onVolumeSetRequest(android.media.MediaRouter.RouteInfo routeInfo, int i) {
            this.mCallback.onVolumeSetRequest(routeInfo, i);
        }

        @Override // android.media.MediaRouter.VolumeCallback
        public void onVolumeUpdateRequest(android.media.MediaRouter.RouteInfo routeInfo, int i) {
            this.mCallback.onVolumeUpdateRequest(routeInfo, i);
        }
    }
}
