package okio;

public interface Socket {
    void cancel();

    Sink getSink();

    Source getSource();
}
