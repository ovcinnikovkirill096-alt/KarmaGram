package androidx.camera.camera2.pipe;

import android.os.Build;
import android.util.Size;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.ws.RealWebSocket;

public interface OutputStream {
    /* JADX INFO: renamed from: getCamera-Dz_R5H8, reason: not valid java name */
    String mo314getCameraDz_R5H8();

    /* JADX INFO: renamed from: getDynamicRangeProfile-OoVcG5w, reason: not valid java name */
    DynamicRangeProfile mo315getDynamicRangeProfileOoVcG5w();

    /* JADX INFO: renamed from: getFormat-8FPWQzE, reason: not valid java name */
    int mo316getFormat8FPWQzE();

    /* JADX INFO: renamed from: getId-4LaLFng, reason: not valid java name */
    int mo317getId4LaLFng();

    /* JADX INFO: renamed from: getMirrorMode-dO1_9xk, reason: not valid java name */
    MirrorMode mo318getMirrorModedO1_9xk();

    OutputType getOutputType();

    Size getSize();

    CameraStream getStream();

    /* JADX INFO: renamed from: getStreamUseCase-8x2ez34, reason: not valid java name */
    StreamUseCase mo319getStreamUseCase8x2ez34();

    /* JADX INFO: renamed from: getStreamUseHint-HIPxoCc, reason: not valid java name */
    StreamUseHint mo320getStreamUseHintHIPxoCc();

    /* JADX INFO: renamed from: getTimestampBase-pcPfPbY, reason: not valid java name */
    TimestampBase mo321getTimestampBasepcPfPbY();

    boolean isValidForHighSpeedOperatingMode();

    public static abstract class Config {
        public static final Companion Companion = new Companion(null);
        private final String camera;
        private final DynamicRangeProfile dynamicRangeProfile;
        private final int format;
        private final MirrorMode mirrorMode;
        private final List sensorPixelModes;
        private final Size size;
        private final StreamUseCase streamUseCase;
        private final StreamUseHint streamUseHint;

        public /* synthetic */ Config(Size size, int i, String str, MirrorMode mirrorMode, TimestampBase timestampBase, DynamicRangeProfile dynamicRangeProfile, StreamUseCase streamUseCase, StreamUseHint streamUseHint, List list, DefaultConstructorMarker defaultConstructorMarker) {
            this(size, i, str, mirrorMode, timestampBase, dynamicRangeProfile, streamUseCase, streamUseHint, list);
        }

        /* JADX INFO: renamed from: getTimestampBase-pcPfPbY, reason: not valid java name */
        public final TimestampBase m328getTimestampBasepcPfPbY() {
            return null;
        }

        private Config(Size size, int i, String str, MirrorMode mirrorMode, TimestampBase timestampBase, DynamicRangeProfile dynamicRangeProfile, StreamUseCase streamUseCase, StreamUseHint streamUseHint, List sensorPixelModes) {
            Intrinsics.checkNotNullParameter(size, "size");
            Intrinsics.checkNotNullParameter(sensorPixelModes, "sensorPixelModes");
            this.size = size;
            this.format = i;
            this.camera = str;
            this.mirrorMode = mirrorMode;
            this.dynamicRangeProfile = dynamicRangeProfile;
            this.streamUseCase = streamUseCase;
            this.streamUseHint = streamUseHint;
            this.sensorPixelModes = sensorPixelModes;
        }

        public final Size getSize() {
            return this.size;
        }

        /* JADX INFO: renamed from: getFormat-8FPWQzE, reason: not valid java name */
        public final int m324getFormat8FPWQzE() {
            return this.format;
        }

        /* JADX INFO: renamed from: getCamera-1LO98Z0, reason: not valid java name */
        public final String m322getCamera1LO98Z0() {
            return this.camera;
        }

        /* JADX INFO: renamed from: getMirrorMode-dO1_9xk, reason: not valid java name */
        public final MirrorMode m325getMirrorModedO1_9xk() {
            return this.mirrorMode;
        }

        /* JADX INFO: renamed from: getDynamicRangeProfile-OoVcG5w, reason: not valid java name */
        public final DynamicRangeProfile m323getDynamicRangeProfileOoVcG5w() {
            return this.dynamicRangeProfile;
        }

        /* JADX INFO: renamed from: getStreamUseCase-8x2ez34, reason: not valid java name */
        public final StreamUseCase m326getStreamUseCase8x2ez34() {
            return this.streamUseCase;
        }

        /* JADX INFO: renamed from: getStreamUseHint-HIPxoCc, reason: not valid java name */
        public final StreamUseHint m327getStreamUseHintHIPxoCc() {
            return this.streamUseHint;
        }

        public final List getSensorPixelModes() {
            return this.sensorPixelModes;
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            /* JADX INFO: renamed from: create-vBYXiEU$default, reason: not valid java name */
            public static /* synthetic */ Config m329createvBYXiEU$default(Companion companion, Size size, int i, String str, OutputType outputType, MirrorMode mirrorMode, TimestampBase timestampBase, DynamicRangeProfile dynamicRangeProfile, StreamUseCase streamUseCase, StreamUseHint streamUseHint, List list, int i2, Object obj) {
                if ((i2 & 4) != 0) {
                    str = null;
                }
                if ((i2 & 8) != 0) {
                    outputType = OutputType.Companion.getSURFACE();
                }
                if ((i2 & 16) != 0) {
                    mirrorMode = null;
                }
                if ((i2 & 32) != 0) {
                    timestampBase = null;
                }
                if ((i2 & 64) != 0) {
                    dynamicRangeProfile = null;
                }
                if ((i2 & 128) != 0) {
                    streamUseCase = null;
                }
                if ((i2 & 256) != 0) {
                    streamUseHint = null;
                }
                if ((i2 & 512) != 0) {
                    list = CollectionsKt.emptyList();
                }
                return companion.m330createvBYXiEU(size, i, str, outputType, mirrorMode, timestampBase, dynamicRangeProfile, streamUseCase, streamUseHint, list);
            }

            /* JADX INFO: renamed from: create-vBYXiEU, reason: not valid java name */
            public final Config m330createvBYXiEU(Size size, int i, String str, OutputType outputType, MirrorMode mirrorMode, TimestampBase timestampBase, DynamicRangeProfile dynamicRangeProfile, StreamUseCase streamUseCase, StreamUseHint streamUseHint, List sensorPixelModes) {
                Intrinsics.checkNotNullParameter(size, "size");
                Intrinsics.checkNotNullParameter(outputType, "outputType");
                Intrinsics.checkNotNullParameter(sensorPixelModes, "sensorPixelModes");
                if (isLazilyConfigurable(outputType)) {
                    return new LazyOutputConfig(size, i, str, outputType, mirrorMode, timestampBase, dynamicRangeProfile, streamUseCase, streamUseHint, sensorPixelModes, null);
                }
                if (!Intrinsics.areEqual(outputType, OutputType.Companion.getSURFACE())) {
                    throw new IllegalStateException("Check failed.");
                }
                return new SimpleOutputConfig(size, i, str, mirrorMode, timestampBase, dynamicRangeProfile, streamUseCase, streamUseHint, sensorPixelModes, null);
            }

            private final boolean isLazilyConfigurable(OutputType outputType) {
                OutputType.Companion companion = OutputType.Companion;
                if (Intrinsics.areEqual(outputType, companion.getSURFACE_TEXTURE()) || Intrinsics.areEqual(outputType, companion.getSURFACE_VIEW())) {
                    return true;
                }
                return (Intrinsics.areEqual(outputType, companion.getMEDIA_CODEC()) || Intrinsics.areEqual(outputType, companion.getMEDIA_RECORDER())) && Build.VERSION.SDK_INT >= 35;
            }
        }

        public static final class SimpleOutputConfig extends Config {
            public /* synthetic */ SimpleOutputConfig(Size size, int i, String str, MirrorMode mirrorMode, TimestampBase timestampBase, DynamicRangeProfile dynamicRangeProfile, StreamUseCase streamUseCase, StreamUseHint streamUseHint, List list, DefaultConstructorMarker defaultConstructorMarker) {
                this(size, i, str, mirrorMode, timestampBase, dynamicRangeProfile, streamUseCase, streamUseHint, list);
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            private SimpleOutputConfig(Size size, int i, String str, MirrorMode mirrorMode, TimestampBase timestampBase, DynamicRangeProfile dynamicRangeProfile, StreamUseCase streamUseCase, StreamUseHint streamUseHint, List sensorPixelModes) {
                super(size, i, str, mirrorMode, timestampBase, dynamicRangeProfile, streamUseCase, streamUseHint, sensorPixelModes, null);
                Intrinsics.checkNotNullParameter(size, "size");
                Intrinsics.checkNotNullParameter(sensorPixelModes, "sensorPixelModes");
            }
        }

        public static final class LazyOutputConfig extends Config {
            private final OutputType outputType;

            public /* synthetic */ LazyOutputConfig(Size size, int i, String str, OutputType outputType, MirrorMode mirrorMode, TimestampBase timestampBase, DynamicRangeProfile dynamicRangeProfile, StreamUseCase streamUseCase, StreamUseHint streamUseHint, List list, DefaultConstructorMarker defaultConstructorMarker) {
                this(size, i, str, outputType, mirrorMode, timestampBase, dynamicRangeProfile, streamUseCase, streamUseHint, list);
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            private LazyOutputConfig(Size size, int i, String str, OutputType outputType, MirrorMode mirrorMode, TimestampBase timestampBase, DynamicRangeProfile dynamicRangeProfile, StreamUseCase streamUseCase, StreamUseHint streamUseHint, List sensorPixelModes) {
                super(size, i, str, mirrorMode, timestampBase, dynamicRangeProfile, streamUseCase, streamUseHint, sensorPixelModes, null);
                Intrinsics.checkNotNullParameter(size, "size");
                Intrinsics.checkNotNullParameter(outputType, "outputType");
                Intrinsics.checkNotNullParameter(sensorPixelModes, "sensorPixelModes");
                this.outputType = outputType;
            }

            public final OutputType getOutputType$camera_camera2_pipe() {
                return this.outputType;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Config(size=");
            sb.append(this.size);
            sb.append(", format=");
            sb.append((Object) StreamFormat.m409toStringimpl(this.format));
            sb.append(", camera=");
            String str = this.camera;
            sb.append((Object) (str == null ? "null" : CameraId.m238toStringimpl(str)));
            sb.append(", mirrorMode=");
            sb.append(this.mirrorMode);
            sb.append(", timestampBase=");
            sb.append((Object) null);
            sb.append(", dynamicRangeProfile=");
            sb.append(this.dynamicRangeProfile);
            sb.append(", streamUseCase=");
            sb.append(this.streamUseCase);
            sb.append(", streamUseHint=");
            sb.append(this.streamUseHint);
            sb.append(", sensorPixelModes=");
            sb.append(this.sensorPixelModes);
            sb.append(')');
            return sb.toString();
        }
    }

    public static final class OutputType {
        public static final Companion Companion = new Companion(null);
        private static final OutputType SURFACE = new OutputType();
        private static final OutputType SURFACE_VIEW = new OutputType();
        private static final OutputType SURFACE_TEXTURE = new OutputType();
        private static final OutputType SURFACE_DEFERRED_FOR_QUERY_ONLY = new OutputType();
        private static final OutputType MEDIA_CODEC = new OutputType();
        private static final OutputType MEDIA_RECORDER = new OutputType();

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final OutputType getSURFACE() {
                return OutputType.SURFACE;
            }

            public final OutputType getSURFACE_VIEW() {
                return OutputType.SURFACE_VIEW;
            }

            public final OutputType getSURFACE_TEXTURE() {
                return OutputType.SURFACE_TEXTURE;
            }

            public final OutputType getSURFACE_DEFERRED_FOR_QUERY_ONLY$camera_camera2_pipe() {
                return OutputType.SURFACE_DEFERRED_FOR_QUERY_ONLY;
            }

            public final OutputType getMEDIA_CODEC() {
                return OutputType.MEDIA_CODEC;
            }

            public final OutputType getMEDIA_RECORDER() {
                return OutputType.MEDIA_RECORDER;
            }
        }

        private OutputType() {
        }
    }

    public static final class MirrorMode {
        private final int value;
        public static final Companion Companion = new Companion(null);
        private static final int MIRROR_MODE_AUTO = m340constructorimpl(0);
        private static final int MIRROR_MODE_NONE = m340constructorimpl(1);
        private static final int MIRROR_MODE_H = m340constructorimpl(2);
        private static final int MIRROR_MODE_V = m340constructorimpl(3);

        /* JADX INFO: renamed from: box-impl, reason: not valid java name */
        public static final /* synthetic */ MirrorMode m339boximpl(int i) {
            return new MirrorMode(i);
        }

        /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
        public static int m340constructorimpl(int i) {
            return i;
        }

        /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
        public static boolean m341equalsimpl(int i, Object obj) {
            return (obj instanceof MirrorMode) && i == ((MirrorMode) obj).m345unboximpl();
        }

        /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
        public static final boolean m342equalsimpl0(int i, int i2) {
            return i == i2;
        }

        /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
        public static int m343hashCodeimpl(int i) {
            return i;
        }

        /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
        public static String m344toStringimpl(int i) {
            return "MirrorMode(value=" + i + ')';
        }

        public boolean equals(Object obj) {
            return m341equalsimpl(this.value, obj);
        }

        public int hashCode() {
            return m343hashCodeimpl(this.value);
        }

        public String toString() {
            return m344toStringimpl(this.value);
        }

        /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
        public final /* synthetic */ int m345unboximpl() {
            return this.value;
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            /* JADX INFO: renamed from: getMIRROR_MODE_AUTO-DrUKqn0, reason: not valid java name */
            public final int m346getMIRROR_MODE_AUTODrUKqn0() {
                return MirrorMode.MIRROR_MODE_AUTO;
            }
        }

        private /* synthetic */ MirrorMode(int i) {
            this.value = i;
        }
    }

    public static final class TimestampBase {
        public static final Companion Companion = new Companion(null);
        private static final int TIMESTAMP_BASE_DEFAULT = m366constructorimpl(0);
        private static final int TIMESTAMP_BASE_SENSOR = m366constructorimpl(1);
        private static final int TIMESTAMP_BASE_MONOTONIC = m366constructorimpl(2);
        private static final int TIMESTAMP_BASE_REALTIME = m366constructorimpl(3);
        private static final int TIMESTAMP_BASE_CHOREOGRAPHER_SYNCED = m366constructorimpl(4);

        /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
        public static int m366constructorimpl(int i) {
            return i;
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            /* JADX INFO: renamed from: getTIMESTAMP_BASE_REALTIME-6HVI0MA, reason: not valid java name */
            public final int m367getTIMESTAMP_BASE_REALTIME6HVI0MA() {
                return TimestampBase.TIMESTAMP_BASE_REALTIME;
            }
        }
    }

    public static final class DynamicRangeProfile {
        private final long value;
        public static final Companion Companion = new Companion(null);
        private static final long STANDARD = m332constructorimpl(1);
        private static final long HLG10 = m332constructorimpl(2);
        private static final long HDR10 = m332constructorimpl(4);
        private static final long HDR10_PLUS = m332constructorimpl(8);
        private static final long DOLBY_VISION_10B_HDR_REF = m332constructorimpl(16);
        private static final long DOLBY_VISION_10B_HDR_REF_PO = m332constructorimpl(32);
        private static final long DOLBY_VISION_10B_HDR_OEM = m332constructorimpl(64);
        private static final long DOLBY_VISION_10B_HDR_OEM_PO = m332constructorimpl(128);
        private static final long DOLBY_VISION_8B_HDR_REF = m332constructorimpl(256);
        private static final long DOLBY_VISION_8B_HDR_REF_PO = m332constructorimpl(512);
        private static final long DOLBY_VISION_8B_HDR_OEM = m332constructorimpl(RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE);
        private static final long DOLBY_VISION_8B_HDR_OEM_PO = m332constructorimpl(2048);
        private static final long PUBLIC_MAX = m332constructorimpl(4096);

        /* JADX INFO: renamed from: box-impl, reason: not valid java name */
        public static final /* synthetic */ DynamicRangeProfile m331boximpl(long j) {
            return new DynamicRangeProfile(j);
        }

        /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
        public static long m332constructorimpl(long j) {
            return j;
        }

        /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
        public static boolean m333equalsimpl(long j, Object obj) {
            return (obj instanceof DynamicRangeProfile) && j == ((DynamicRangeProfile) obj).m337unboximpl();
        }

        /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
        public static final boolean m334equalsimpl0(long j, long j2) {
            return j == j2;
        }

        /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
        public static int m335hashCodeimpl(long j) {
            return CameraTimestamp$$ExternalSyntheticBackport0.m(j);
        }

        /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
        public static String m336toStringimpl(long j) {
            return "DynamicRangeProfile(value=" + j + ')';
        }

        public boolean equals(Object obj) {
            return m333equalsimpl(this.value, obj);
        }

        public int hashCode() {
            return m335hashCodeimpl(this.value);
        }

        public String toString() {
            return m336toStringimpl(this.value);
        }

        /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
        public final /* synthetic */ long m337unboximpl() {
            return this.value;
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            /* JADX INFO: renamed from: getSTANDARD-fFAQAUE, reason: not valid java name */
            public final long m338getSTANDARDfFAQAUE() {
                return DynamicRangeProfile.STANDARD;
            }
        }

        private /* synthetic */ DynamicRangeProfile(long j) {
            this.value = j;
        }
    }

    public static final class StreamUseHint {
        public static final Companion Companion = new Companion(null);
        private static final long DEFAULT = m358constructorimpl(0);
        private static final long VIDEO_RECORD = m358constructorimpl(1);
        private final long value;

        /* JADX INFO: renamed from: box-impl, reason: not valid java name */
        public static final /* synthetic */ StreamUseHint m357boximpl(long j) {
            return new StreamUseHint(j);
        }

        /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
        public static long m358constructorimpl(long j) {
            return j;
        }

        /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
        public static boolean m359equalsimpl(long j, Object obj) {
            return (obj instanceof StreamUseHint) && j == ((StreamUseHint) obj).m363unboximpl();
        }

        /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
        public static final boolean m360equalsimpl0(long j, long j2) {
            return j == j2;
        }

        /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
        public static int m361hashCodeimpl(long j) {
            return CameraTimestamp$$ExternalSyntheticBackport0.m(j);
        }

        /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
        public static String m362toStringimpl(long j) {
            return "StreamUseHint(value=" + j + ')';
        }

        public boolean equals(Object obj) {
            return m359equalsimpl(this.value, obj);
        }

        public int hashCode() {
            return m361hashCodeimpl(this.value);
        }

        public String toString() {
            return m362toStringimpl(this.value);
        }

        /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
        public final /* synthetic */ long m363unboximpl() {
            return this.value;
        }

        private /* synthetic */ StreamUseHint(long j) {
            this.value = j;
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            /* JADX INFO: renamed from: getDEFAULT-4VYZOf8, reason: not valid java name */
            public final long m364getDEFAULT4VYZOf8() {
                return StreamUseHint.DEFAULT;
            }

            /* JADX INFO: renamed from: getVIDEO_RECORD-4VYZOf8, reason: not valid java name */
            public final long m365getVIDEO_RECORD4VYZOf8() {
                return StreamUseHint.VIDEO_RECORD;
            }
        }
    }

    public static final class StreamUseCase {
        private final long value;
        public static final Companion Companion = new Companion(null);
        private static final long DEFAULT = m348constructorimpl(0);
        private static final long PREVIEW = m348constructorimpl(1);
        private static final long STILL_CAPTURE = m348constructorimpl(2);
        private static final long VIDEO_RECORD = m348constructorimpl(3);
        private static final long PREVIEW_VIDEO_STILL = m348constructorimpl(4);
        private static final long VIDEO_CALL = m348constructorimpl(5);
        private static final long CROPPED_RAW = m348constructorimpl(6);

        /* JADX INFO: renamed from: box-impl, reason: not valid java name */
        public static final /* synthetic */ StreamUseCase m347boximpl(long j) {
            return new StreamUseCase(j);
        }

        /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
        public static long m348constructorimpl(long j) {
            return j;
        }

        /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
        public static boolean m349equalsimpl(long j, Object obj) {
            return (obj instanceof StreamUseCase) && j == ((StreamUseCase) obj).m353unboximpl();
        }

        /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
        public static final boolean m350equalsimpl0(long j, long j2) {
            return j == j2;
        }

        /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
        public static int m351hashCodeimpl(long j) {
            return CameraTimestamp$$ExternalSyntheticBackport0.m(j);
        }

        /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
        public static String m352toStringimpl(long j) {
            return "StreamUseCase(value=" + j + ')';
        }

        public boolean equals(Object obj) {
            return m349equalsimpl(this.value, obj);
        }

        public int hashCode() {
            return m351hashCodeimpl(this.value);
        }

        public String toString() {
            return m352toStringimpl(this.value);
        }

        /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
        public final /* synthetic */ long m353unboximpl() {
            return this.value;
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            /* JADX INFO: renamed from: getDEFAULT-vrKr8v8, reason: not valid java name */
            public final long m354getDEFAULTvrKr8v8() {
                return StreamUseCase.DEFAULT;
            }

            /* JADX INFO: renamed from: getPREVIEW-vrKr8v8, reason: not valid java name */
            public final long m355getPREVIEWvrKr8v8() {
                return StreamUseCase.PREVIEW;
            }

            /* JADX INFO: renamed from: getVIDEO_RECORD-vrKr8v8, reason: not valid java name */
            public final long m356getVIDEO_RECORDvrKr8v8() {
                return StreamUseCase.VIDEO_RECORD;
            }
        }

        private /* synthetic */ StreamUseCase(long j) {
            this.value = j;
        }
    }

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.OutputStream$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        public static boolean $default$isValidForHighSpeedOperatingMode(OutputStream outputStream) {
            if (outputStream.mo319getStreamUseCase8x2ez34() == null) {
                return true;
            }
            StreamUseCase streamUseCaseMo319getStreamUseCase8x2ez34 = outputStream.mo319getStreamUseCase8x2ez34();
            StreamUseCase.Companion companion = StreamUseCase.Companion;
            if (streamUseCaseMo319getStreamUseCase8x2ez34 == null ? false : StreamUseCase.m350equalsimpl0(streamUseCaseMo319getStreamUseCase8x2ez34.m353unboximpl(), companion.m354getDEFAULTvrKr8v8())) {
                return true;
            }
            StreamUseCase streamUseCaseMo319getStreamUseCase8x2ez35 = outputStream.mo319getStreamUseCase8x2ez34();
            if (streamUseCaseMo319getStreamUseCase8x2ez35 == null ? false : StreamUseCase.m350equalsimpl0(streamUseCaseMo319getStreamUseCase8x2ez35.m353unboximpl(), companion.m355getPREVIEWvrKr8v8())) {
                return true;
            }
            StreamUseCase streamUseCaseMo319getStreamUseCase8x2ez36 = outputStream.mo319getStreamUseCase8x2ez34();
            if ((streamUseCaseMo319getStreamUseCase8x2ez36 == null ? false : StreamUseCase.m350equalsimpl0(streamUseCaseMo319getStreamUseCase8x2ez36.m353unboximpl(), companion.m356getVIDEO_RECORDvrKr8v8())) || outputStream.mo320getStreamUseHintHIPxoCc() == null) {
                return true;
            }
            StreamUseHint streamUseHintMo320getStreamUseHintHIPxoCc = outputStream.mo320getStreamUseHintHIPxoCc();
            StreamUseHint.Companion companion2 = StreamUseHint.Companion;
            if (streamUseHintMo320getStreamUseHintHIPxoCc == null ? false : StreamUseHint.m360equalsimpl0(streamUseHintMo320getStreamUseHintHIPxoCc.m363unboximpl(), companion2.m364getDEFAULT4VYZOf8())) {
                return true;
            }
            StreamUseHint streamUseHintMo320getStreamUseHintHIPxoCc2 = outputStream.mo320getStreamUseHintHIPxoCc();
            return streamUseHintMo320getStreamUseHintHIPxoCc2 == null ? false : StreamUseHint.m360equalsimpl0(streamUseHintMo320getStreamUseHintHIPxoCc2.m363unboximpl(), companion2.m365getVIDEO_RECORD4VYZOf8());
        }
    }
}
