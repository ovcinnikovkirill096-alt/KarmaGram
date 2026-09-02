package kotlinx.serialization.modules;

import kotlin.collections.MapsKt;

public abstract class SerializersModuleKt {
    private static final SerializersModule EmptySerializersModule = new SerialModuleImpl(MapsKt.emptyMap(), MapsKt.emptyMap(), MapsKt.emptyMap(), MapsKt.emptyMap(), MapsKt.emptyMap(), false);

    public static final SerializersModule getEmptySerializersModule() {
        return EmptySerializersModule;
    }
}
