package org.telegram.tgnet;

public final /* synthetic */ class TLRPC$TL_messages_quickReplies$$ExternalSyntheticLambda0 implements Vector.TLDeserializer {
    @Override // org.telegram.tgnet.Vector.TLDeserializer
    public final TLObject deserialize(InputSerializedData inputSerializedData, int i, boolean z) {
        return TLRPC.TL_quickReply.TLdeserialize(inputSerializedData, i, z);
    }
}
