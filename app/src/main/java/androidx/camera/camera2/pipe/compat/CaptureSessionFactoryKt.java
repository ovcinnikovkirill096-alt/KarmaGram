package androidx.camera.camera2.pipe.compat;

import android.hardware.camera2.params.OutputConfiguration;
import android.os.Build;
import android.util.Size;
import android.view.Surface;
import androidx.camera.camera2.pipe.CameraGraph;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.CameraStream;
import androidx.camera.camera2.pipe.OutputId;
import androidx.camera.camera2.pipe.OutputStream;
import androidx.camera.camera2.pipe.StreamId;
import androidx.camera.camera2.pipe.core.Log;
import androidx.camera.camera2.pipe.graph.StreamGraphImpl;
import androidx.camera.camera2.pipe.media.AndroidMultiResolutionImageReader;
import androidx.camera.camera2.pipe.media.ImageSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

public abstract class CaptureSessionFactoryKt {
    public static final OutputConfigurations buildOutputConfigurations(CameraGraph.Config graphConfig, StreamGraphImpl streamGraph, Map surfaces) {
        Intrinsics.checkNotNullParameter(graphConfig, "graphConfig");
        Intrinsics.checkNotNullParameter(streamGraph, "streamGraph");
        Intrinsics.checkNotNullParameter(surfaces, "surfaces");
        ArrayList arrayList = new ArrayList();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        LinkedHashMap linkedHashMap3 = new LinkedHashMap();
        for (Map.Entry entry : streamGraph.getImageSourceMap$camera_camera2_pipe().entrySet()) {
            int iM423unboximpl = ((StreamId) entry.getKey()).m423unboximpl();
            ImageSource imageSource = (ImageSource) entry.getValue();
            CameraStream cameraStreamMo413getaKI5c8E = streamGraph.mo413getaKI5c8E(iM423unboximpl);
            if (cameraStreamMo413getaKI5c8E == null) {
                throw new IllegalStateException("Required value was null.");
            }
            List outputs = cameraStreamMo413getaKI5c8E.getOutputs();
            if (outputs.size() != 1) {
                if (Build.VERSION.SDK_INT < 31) {
                    throw new IllegalArgumentException("Cannot configure multiple outputs pre-S!");
                }
                Object objUnwrapAs = imageSource.unwrapAs(Reflection.getOrCreateKotlinClass(AndroidMultiResolutionImageReader.class));
                if (objUnwrapAs == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                List outputConfigurations$camera_camera2_pipe = ((AndroidMultiResolutionImageReader) objUnwrapAs).getOutputConfigurations$camera_camera2_pipe();
                if (outputConfigurations$camera_camera2_pipe.size() != outputs.size()) {
                    throw new IllegalStateException("Check failed.");
                }
                int size = outputs.size();
                for (int i = 0; i < size; i++) {
                    OutputStream outputStream = (OutputStream) outputs.get(i);
                    OutputConfiguration outputConfigurationM = AndroidCameraCaptureSession$$ExternalSyntheticApiModelOutline1.m(outputConfigurations$camera_camera2_pipe.get(i));
                    Object obj = streamGraph.getOutputConfigMap$camera_camera2_pipe().get(outputStream);
                    if (obj == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    StreamGraphImpl.OutputConfig outputConfig = (StreamGraphImpl.OutputConfig) obj;
                    if (outputConfig.getExternalOutputConfig() != null) {
                        throw new IllegalStateException("External OutputConfiguration shouldn't be set in multi-output streams configured with ImageSource.Config");
                    }
                    linkedHashMap3.put(outputConfig, outputConfigurationM);
                }
            }
        }
        for (CameraStream cameraStream : streamGraph.getStreams()) {
            List<OutputStream> outputs2 = cameraStream.getOutputs();
            if (outputs2.size() == 1) {
                Surface surface = (Surface) surfaces.get(StreamId.m417boximpl(cameraStream.m247getIdptHMqGs()));
                if (surface != null) {
                    linkedHashMap2.put(OutputId.m296boximpl(((OutputStream) CollectionsKt.single(outputs2)).mo317getId4LaLFng()), surface);
                }
            } else {
                for (OutputStream outputStream2 : outputs2) {
                    Object obj2 = streamGraph.getOutputConfigMap$camera_camera2_pipe().get(outputStream2);
                    if (obj2 == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    StreamGraphImpl.OutputConfig outputConfig2 = (StreamGraphImpl.OutputConfig) obj2;
                    OutputConfiguration externalOutputConfig = outputConfig2.getExternalOutputConfig();
                    if (externalOutputConfig == null) {
                        externalOutputConfig = AndroidCameraCaptureSession$$ExternalSyntheticApiModelOutline1.m(linkedHashMap3.get(outputConfig2));
                    }
                    Surface surface2 = externalOutputConfig != null ? externalOutputConfig.getSurface() : (Surface) surfaces.get(StreamId.m417boximpl(cameraStream.m247getIdptHMqGs()));
                    if (surface2 != null) {
                        linkedHashMap2.put(OutputId.m296boximpl(outputStream2.mo317getId4LaLFng()), surface2);
                    }
                }
            }
        }
        OutputConfigurationWrapper outputConfigurationWrapper = null;
        for (StreamGraphImpl.OutputConfig outputConfig3 : streamGraph.getOutputConfigs$camera_camera2_pipe()) {
            List streams = outputConfig3.getStreams();
            ArrayList arrayList2 = new ArrayList();
            Iterator it = streams.iterator();
            while (it.hasNext()) {
                Surface surface3 = (Surface) surfaces.get(StreamId.m417boximpl(((CameraStream) it.next()).m247getIdptHMqGs()));
                if (surface3 != null) {
                    arrayList2.add(surface3);
                }
            }
            OutputConfiguration externalOutputConfig2 = outputConfig3.getExternalOutputConfig();
            if (externalOutputConfig2 == null) {
                externalOutputConfig2 = AndroidCameraCaptureSession$$ExternalSyntheticApiModelOutline1.m(linkedHashMap3.get(outputConfig3));
            }
            OutputConfiguration outputConfiguration = externalOutputConfig2;
            if (outputConfiguration != null) {
                if (arrayList2.size() != outputConfig3.getStreams().size()) {
                    List streams2 = outputConfig3.getStreams();
                    ArrayList arrayList3 = new ArrayList();
                    for (Object obj3 : streams2) {
                        if (!surfaces.containsKey(StreamId.m417boximpl(((CameraStream) obj3).m247getIdptHMqGs()))) {
                            arrayList3.add(obj3);
                        }
                    }
                    throw new IllegalStateException(("Surfaces are not yet available for " + outputConfig3 + "! Missing surfaces for " + arrayList3 + '!').toString());
                }
                arrayList.add(new AndroidOutputConfiguration(outputConfiguration, false, 1, null, null));
            } else if (outputConfig3.getDeferrable() && arrayList2.size() != outputConfig3.getStreams().size()) {
                AndroidOutputConfiguration.Companion companion = AndroidOutputConfiguration.Companion;
                Size size2 = outputConfig3.getSize();
                OutputStream.OutputType deferredOutputType = outputConfig3.getDeferredOutputType();
                Intrinsics.checkNotNull(deferredOutputType);
                OutputStream.MirrorMode mirrorModeM562getMirrorModedO1_9xk = outputConfig3.m562getMirrorModedO1_9xk();
                outputConfig3.m565getTimestampBasepcPfPbY();
                OutputStream.DynamicRangeProfile dynamicRangeProfileM560getDynamicRangeProfileOoVcG5w = outputConfig3.m560getDynamicRangeProfileOoVcG5w();
                OutputStream.StreamUseCase streamUseCaseM563getStreamUseCase8x2ez34 = outputConfig3.m563getStreamUseCase8x2ez34();
                List sensorPixelModes = outputConfig3.getSensorPixelModes();
                boolean surfaceSharing = outputConfig3.getSurfaceSharing();
                Integer groupNumber = outputConfig3.getGroupNumber();
                OutputConfigurationWrapper outputConfigurationWrapperM437creategWWoySg$default = AndroidOutputConfiguration.Companion.m437creategWWoySg$default(companion, null, null, deferredOutputType, mirrorModeM562getMirrorModedO1_9xk, null, dynamicRangeProfileM560getDynamicRangeProfileOoVcG5w, streamUseCaseM563getStreamUseCase8x2ez34, sensorPixelModes, size2, surfaceSharing, groupNumber != null ? groupNumber.intValue() : -1, !CameraId.m236equalsimpl0(outputConfig3.m559getCameraDz_R5H8(), graphConfig.m206getCameraDz_R5H8()) ? outputConfig3.m559getCameraDz_R5H8() : null, 2, null);
                if (outputConfigurationWrapperM437creategWWoySg$default != null) {
                    arrayList.add(outputConfigurationWrapperM437creategWWoySg$default);
                    Iterator it2 = outputConfig3.getStreamBuilder$camera_camera2_pipe().iterator();
                    while (it2.hasNext()) {
                        linkedHashMap.put(StreamId.m417boximpl(((CameraStream) it2.next()).m247getIdptHMqGs()), outputConfigurationWrapperM437creategWWoySg$default);
                    }
                } else if (Log.INSTANCE.getWARN_LOGGABLE()) {
                    android.util.Log.w("CXCP", "Failed to create AndroidOutputConfiguration for " + outputConfig3);
                }
            } else {
                if (arrayList2.size() != outputConfig3.getStreams().size()) {
                    List streams3 = outputConfig3.getStreams();
                    ArrayList arrayList4 = new ArrayList();
                    for (Object obj4 : streams3) {
                        if (!surfaces.containsKey(StreamId.m417boximpl(((CameraStream) obj4).m247getIdptHMqGs()))) {
                            arrayList4.add(obj4);
                        }
                    }
                    throw new IllegalStateException(("Surfaces are not yet available for " + outputConfig3 + "! Missing surfaces for " + arrayList4 + '!').toString());
                }
                AndroidOutputConfiguration.Companion companion2 = AndroidOutputConfiguration.Companion;
                Surface surface4 = (Surface) CollectionsKt.first((List) arrayList2);
                OutputStream.MirrorMode mirrorModeM562getMirrorModedO1_9xk2 = outputConfig3.m562getMirrorModedO1_9xk();
                outputConfig3.m565getTimestampBasepcPfPbY();
                OutputStream.DynamicRangeProfile dynamicRangeProfileM560getDynamicRangeProfileOoVcG5w2 = outputConfig3.m560getDynamicRangeProfileOoVcG5w();
                OutputStream.StreamUseCase streamUseCaseM563getStreamUseCase8x2ez35 = outputConfig3.m563getStreamUseCase8x2ez34();
                List sensorPixelModes2 = outputConfig3.getSensorPixelModes();
                Size size3 = outputConfig3.getSize();
                boolean surfaceSharing2 = outputConfig3.getSurfaceSharing();
                Integer groupNumber2 = outputConfig3.getGroupNumber();
                OutputConfigurationWrapper outputConfigurationWrapperM437creategWWoySg$default2 = AndroidOutputConfiguration.Companion.m437creategWWoySg$default(companion2, surface4, null, null, mirrorModeM562getMirrorModedO1_9xk2, null, dynamicRangeProfileM560getDynamicRangeProfileOoVcG5w2, streamUseCaseM563getStreamUseCase8x2ez35, sensorPixelModes2, size3, surfaceSharing2, groupNumber2 != null ? groupNumber2.intValue() : -1, !CameraId.m236equalsimpl0(outputConfig3.m559getCameraDz_R5H8(), graphConfig.m206getCameraDz_R5H8()) ? outputConfig3.m559getCameraDz_R5H8() : null, 6, null);
                if (outputConfigurationWrapperM437creategWWoySg$default2 != null) {
                    Iterator it3 = CollectionsKt.drop(arrayList2, 1).iterator();
                    while (it3.hasNext()) {
                        outputConfigurationWrapperM437creategWWoySg$default2.addSurface((Surface) it3.next());
                    }
                    if (graphConfig.getPostviewStream() != null) {
                        CameraStream cameraStream2 = streamGraph.get(graphConfig.getPostviewStream());
                        if (cameraStream2 == null) {
                            throw new IllegalStateException("Postview Stream in StreamGraph cannot be null for reprocessing request");
                        }
                        if (outputConfigurationWrapper == null && outputConfig3.getStreams().contains(cameraStream2)) {
                            outputConfigurationWrapper = outputConfigurationWrapperM437creategWWoySg$default2;
                        } else {
                            arrayList.add(outputConfigurationWrapperM437creategWWoySg$default2);
                        }
                    } else {
                        arrayList.add(outputConfigurationWrapperM437creategWWoySg$default2);
                    }
                } else if (Log.INSTANCE.getWARN_LOGGABLE()) {
                    android.util.Log.w("CXCP", "Failed to create AndroidOutputConfiguration for " + outputConfig3);
                }
            }
        }
        return new OutputConfigurations(arrayList, linkedHashMap, outputConfigurationWrapper, linkedHashMap2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Map buildSimpleOutputSurfaceMap(Map map, StreamGraphImpl streamGraphImpl) {
        Map mapCreateMapBuilder = MapsKt.createMapBuilder();
        for (CameraStream cameraStream : streamGraphImpl.getStreams()) {
            Surface surface = (Surface) map.get(StreamId.m417boximpl(cameraStream.m247getIdptHMqGs()));
            if (surface != null) {
                Iterator it = cameraStream.getOutputs().iterator();
                while (it.hasNext()) {
                    mapCreateMapBuilder.put(OutputId.m296boximpl(((OutputStream) it.next()).mo317getId4LaLFng()), surface);
                }
            }
        }
        return MapsKt.build(mapCreateMapBuilder);
    }
}
