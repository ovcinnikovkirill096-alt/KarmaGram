package kotlinx.serialization.descriptors;

import kotlin.jvm.internal.Intrinsics;

public abstract class SerialDescriptorKt {
    public static final Iterable getElementDescriptors(SerialDescriptor serialDescriptor) {
        Intrinsics.checkNotNullParameter(serialDescriptor, "<this>");
        return new SerialDescriptorKt$special$$inlined$Iterable$1(serialDescriptor);
    }
}
