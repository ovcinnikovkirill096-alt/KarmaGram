package androidx.camera.core.featuregroup.impl;

import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.view.SurfaceHolder;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.core.featuregroup.impl.feature.FeatureTypeInternal;
import androidx.camera.core.impl.ImageCaptureConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.utils.UseCaseUtil;
import androidx.camera.core.streamsharing.StreamSharing;
import java.util.Iterator;
import kotlin.NoWhenBranchMatchedException;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public enum UseCaseType {
    PREVIEW(SurfaceHolder.class),
    IMAGE_CAPTURE(null),
    IMAGE_ANALYSIS(null),
    VIDEO_CAPTURE(MediaCodec.class),
    STREAM_SHARING(SurfaceTexture.class),
    UNDEFINED(null);

    private final Class surfaceClass;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    public static final Companion Companion = new Companion(null);

    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[UseCaseType.values().length];
            try {
                iArr[UseCaseType.PREVIEW.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[UseCaseType.IMAGE_CAPTURE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[UseCaseType.IMAGE_ANALYSIS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[UseCaseType.VIDEO_CAPTURE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[UseCaseType.STREAM_SHARING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[UseCaseType.UNDEFINED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    UseCaseType(Class cls) {
        this.surfaceClass = cls;
    }

    public final Class getSurfaceClass() {
        return this.surfaceClass;
    }

    @Override // java.lang.Enum
    public String toString() {
        switch (WhenMappings.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                return "Preview";
            case 2:
                return "ImageCapture";
            case 3:
                return "ImageAnalysis";
            case 4:
                return "VideoCapture";
            case 5:
                return "StreamSharing";
            case 6:
                return "Undefined";
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    public static final class Companion {

        public static final /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;
            public static final /* synthetic */ int[] $EnumSwitchMapping$1;

            static {
                int[] iArr = new int[UseCaseConfigFactory.CaptureType.values().length];
                try {
                    iArr[UseCaseConfigFactory.CaptureType.IMAGE_ANALYSIS.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[UseCaseConfigFactory.CaptureType.IMAGE_CAPTURE.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[UseCaseConfigFactory.CaptureType.PREVIEW.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                try {
                    iArr[UseCaseConfigFactory.CaptureType.VIDEO_CAPTURE.ordinal()] = 4;
                } catch (NoSuchFieldError unused4) {
                }
                try {
                    iArr[UseCaseConfigFactory.CaptureType.STREAM_SHARING.ordinal()] = 5;
                } catch (NoSuchFieldError unused5) {
                }
                $EnumSwitchMapping$0 = iArr;
                int[] iArr2 = new int[FeatureTypeInternal.values().length];
                try {
                    iArr2[FeatureTypeInternal.DYNAMIC_RANGE.ordinal()] = 1;
                } catch (NoSuchFieldError unused6) {
                }
                try {
                    iArr2[FeatureTypeInternal.FPS_RANGE.ordinal()] = 2;
                } catch (NoSuchFieldError unused7) {
                }
                try {
                    iArr2[FeatureTypeInternal.VIDEO_STABILIZATION.ordinal()] = 3;
                } catch (NoSuchFieldError unused8) {
                }
                try {
                    iArr2[FeatureTypeInternal.IMAGE_FORMAT.ordinal()] = 4;
                } catch (NoSuchFieldError unused9) {
                }
                try {
                    iArr2[FeatureTypeInternal.RECORDING_QUALITY.ordinal()] = 5;
                } catch (NoSuchFieldError unused10) {
                }
                $EnumSwitchMapping$1 = iArr2;
            }
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final UseCaseType getFeatureGroupUseCaseType(UseCase useCase) {
            Intrinsics.checkNotNullParameter(useCase, "<this>");
            if (useCase instanceof Preview) {
                return UseCaseType.PREVIEW;
            }
            if (useCase instanceof ImageCapture) {
                return UseCaseType.IMAGE_CAPTURE;
            }
            if (UseCaseUtil.isVideoCapture(useCase)) {
                return UseCaseType.VIDEO_CAPTURE;
            }
            if (useCase instanceof StreamSharing) {
                return UseCaseType.STREAM_SHARING;
            }
            return UseCaseType.UNDEFINED;
        }

        public final UseCaseType getFeatureGroupUseCaseType(UseCaseConfig useCaseConfig) {
            Intrinsics.checkNotNullParameter(useCaseConfig, "<this>");
            int i = WhenMappings.$EnumSwitchMapping$0[useCaseConfig.getCaptureType().ordinal()];
            if (i == 1) {
                return UseCaseType.IMAGE_ANALYSIS;
            }
            if (i == 2) {
                return UseCaseType.IMAGE_CAPTURE;
            }
            if (i == 3) {
                return UseCaseType.PREVIEW;
            }
            if (i == 4) {
                return UseCaseType.VIDEO_CAPTURE;
            }
            if (i == 5) {
                return UseCaseType.STREAM_SHARING;
            }
            return UseCaseType.UNDEFINED;
        }

        public final FeatureTypeInternal getAppConfiguredGroupableFeatureType$camera_core(UseCase useCase) {
            Object next;
            Intrinsics.checkNotNullParameter(useCase, "<this>");
            Iterator<E> it = FeatureTypeInternal.getEntries().iterator();
            while (it.hasNext()) {
                next = it.next();
                if (UseCaseType.Companion.isConfiguredToUseCaseByApp((FeatureTypeInternal) next, useCase)) {
                    return (FeatureTypeInternal) next;
                }
            }
            next = null;
            return (FeatureTypeInternal) next;
        }

        private final boolean isConfiguredToUseCaseByApp(FeatureTypeInternal featureTypeInternal, UseCase useCase) {
            int i = WhenMappings.$EnumSwitchMapping$1[featureTypeInternal.ordinal()];
            if (i == 1) {
                return isDynamicRangeConfiguredByApp(useCase);
            }
            if (i == 2) {
                return isFpsRangeConfiguredByApp(useCase);
            }
            if (i == 3) {
                return isStabilizationModeConfiguredByApp(useCase);
            }
            if (i == 4) {
                return isImageFormatConfiguredByApp(useCase);
            }
            if (i != 5) {
                throw new NoWhenBranchMatchedException();
            }
            return isRecordingQualityConfiguredByApp(useCase);
        }

        private final boolean isDynamicRangeConfiguredByApp(UseCase useCase) {
            return useCase.getAppConfig().hasDynamicRange();
        }

        private final boolean isFpsRangeConfiguredByApp(UseCase useCase) {
            return useCase.getAppConfig().hasTargetFrameRate();
        }

        private final boolean isStabilizationModeConfiguredByApp(UseCase useCase) {
            return useCase.getAppConfig().containsOption(UseCaseConfig.OPTION_PREVIEW_STABILIZATION_MODE) || useCase.getAppConfig().containsOption(UseCaseConfig.OPTION_VIDEO_STABILIZATION_MODE);
        }

        private final boolean isImageFormatConfiguredByApp(UseCase useCase) {
            return useCase.getAppConfig().containsOption(ImageCaptureConfig.OPTION_OUTPUT_FORMAT);
        }

        private final boolean isRecordingQualityConfiguredByApp(UseCase useCase) {
            return Intrinsics.areEqual(useCase.getAppConfig().retrieveOption(UseCaseConfig.OPTION_IS_VIDEO_QUALITY_SELECTOR_DEFAULT, Boolean.TRUE), Boolean.FALSE);
        }
    }
}
