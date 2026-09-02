package androidx.camera.core.imagecapture;

import android.util.Size;
import androidx.camera.core.ImageReaderProxyProvider;
import androidx.camera.core.processing.Edge;
import java.util.List;

final class AutoValue_CaptureNode_In extends CaptureNode.In {
    private final Edge errorEdge;
    private final int inputFormat;
    private final List outputFormats;
    private final PostviewSettings postviewSettings;
    private final Edge requestEdge;
    private final Size size;
    private final boolean virtualCamera;

    @Override // androidx.camera.core.imagecapture.CaptureNode.In
    ImageReaderProxyProvider getImageReaderProxyProvider() {
        return null;
    }

    AutoValue_CaptureNode_In(Size size, int i, List list, boolean z, ImageReaderProxyProvider imageReaderProxyProvider, PostviewSettings postviewSettings, Edge edge, Edge edge2) {
        if (size == null) {
            throw new NullPointerException("Null size");
        }
        this.size = size;
        this.inputFormat = i;
        if (list == null) {
            throw new NullPointerException("Null outputFormats");
        }
        this.outputFormats = list;
        this.virtualCamera = z;
        this.postviewSettings = postviewSettings;
        if (edge == null) {
            throw new NullPointerException("Null requestEdge");
        }
        this.requestEdge = edge;
        if (edge2 == null) {
            throw new NullPointerException("Null errorEdge");
        }
        this.errorEdge = edge2;
    }

    @Override // androidx.camera.core.imagecapture.CaptureNode.In
    Size getSize() {
        return this.size;
    }

    @Override // androidx.camera.core.imagecapture.CaptureNode.In
    int getInputFormat() {
        return this.inputFormat;
    }

    @Override // androidx.camera.core.imagecapture.CaptureNode.In
    List getOutputFormats() {
        return this.outputFormats;
    }

    @Override // androidx.camera.core.imagecapture.CaptureNode.In
    boolean isVirtualCamera() {
        return this.virtualCamera;
    }

    @Override // androidx.camera.core.imagecapture.CaptureNode.In
    PostviewSettings getPostviewSettings() {
        return this.postviewSettings;
    }

    @Override // androidx.camera.core.imagecapture.CaptureNode.In
    Edge getRequestEdge() {
        return this.requestEdge;
    }

    @Override // androidx.camera.core.imagecapture.CaptureNode.In
    Edge getErrorEdge() {
        return this.errorEdge;
    }

    public String toString() {
        return "In{size=" + this.size + ", inputFormat=" + this.inputFormat + ", outputFormats=" + this.outputFormats + ", virtualCamera=" + this.virtualCamera + ", imageReaderProxyProvider=" + ((Object) null) + ", postviewSettings=" + this.postviewSettings + ", requestEdge=" + this.requestEdge + ", errorEdge=" + this.errorEdge + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CaptureNode.In) {
            CaptureNode.In in = (CaptureNode.In) obj;
            if (this.size.equals(in.getSize()) && this.inputFormat == in.getInputFormat() && this.outputFormats.equals(in.getOutputFormats()) && this.virtualCamera == in.isVirtualCamera()) {
                in.getImageReaderProxyProvider();
                in.getPostviewSettings();
                if (this.requestEdge.equals(in.getRequestEdge()) && this.errorEdge.equals(in.getErrorEdge())) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        return ((((((((((((this.size.hashCode() ^ 1000003) * 1000003) ^ this.inputFormat) * 1000003) ^ this.outputFormats.hashCode()) * 1000003) ^ (this.virtualCamera ? 1231 : 1237)) * (-721379959)) ^ 0) * 1000003) ^ this.requestEdge.hashCode()) * 1000003) ^ this.errorEdge.hashCode();
    }
}
