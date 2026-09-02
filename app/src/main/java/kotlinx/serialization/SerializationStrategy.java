package kotlinx.serialization;

import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.encoding.Encoder;

public interface SerializationStrategy {
    SerialDescriptor getDescriptor();

    void serialize(Encoder encoder, Object obj);
}
