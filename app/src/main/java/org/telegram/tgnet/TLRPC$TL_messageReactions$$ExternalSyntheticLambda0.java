package org.telegram.tgnet;

public final /* synthetic */ class TLRPC$TL_messageReactions$$ExternalSyntheticLambda0 implements Vector.TLDeserializer {
    @Override // org.telegram.tgnet.Vector.TLDeserializer
    public final TLObject deserialize(InputSerializedData inputSerializedData, int i, boolean z) {
        return TLRPC.ReactionCount.TLdeserialize(inputSerializedData, i, z);
    }
}
