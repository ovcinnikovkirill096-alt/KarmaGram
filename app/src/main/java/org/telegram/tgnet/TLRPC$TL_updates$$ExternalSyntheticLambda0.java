package org.telegram.tgnet;

public final /* synthetic */ class TLRPC$TL_updates$$ExternalSyntheticLambda0 implements Vector.TLDeserializer {
    @Override // org.telegram.tgnet.Vector.TLDeserializer
    public final TLObject deserialize(InputSerializedData inputSerializedData, int i, boolean z) {
        return TLRPC.Update.TLdeserialize(inputSerializedData, i, z);
    }
}
