package org.telegram.tgnet;

public final /* synthetic */ class TLRPC$TL_inputMediaPaidMedia$$ExternalSyntheticLambda0 implements Vector.TLDeserializer {
    @Override // org.telegram.tgnet.Vector.TLDeserializer
    public final TLObject deserialize(InputSerializedData inputSerializedData, int i, boolean z) {
        return TLRPC.InputMedia.TLdeserialize(inputSerializedData, i, z);
    }
}
