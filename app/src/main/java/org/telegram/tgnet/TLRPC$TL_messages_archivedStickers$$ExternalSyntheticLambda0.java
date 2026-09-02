package org.telegram.tgnet;

public final /* synthetic */ class TLRPC$TL_messages_archivedStickers$$ExternalSyntheticLambda0 implements Vector.TLDeserializer {
    @Override // org.telegram.tgnet.Vector.TLDeserializer
    public final TLObject deserialize(InputSerializedData inputSerializedData, int i, boolean z) {
        return TLRPC.StickerSetCovered.TLdeserialize(inputSerializedData, i, z);
    }
}
