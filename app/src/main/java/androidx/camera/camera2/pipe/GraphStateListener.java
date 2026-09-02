package androidx.camera.camera2.pipe;

public interface GraphStateListener {
    void onGraphError(GraphState.GraphStateError graphStateError);

    void onGraphStarted();

    void onGraphStarting();

    void onGraphStopped();

    void onGraphStopping();
}
