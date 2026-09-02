package kotlinx.serialization.json.internal;

public interface InternalJsonWriter {
    void write(String str);

    void writeChar(char c);

    void writeLong(long j);

    void writeQuoted(String str);
}
