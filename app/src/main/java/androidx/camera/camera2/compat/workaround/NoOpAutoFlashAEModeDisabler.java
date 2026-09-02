package androidx.camera.camera2.compat.workaround;

public final class NoOpAutoFlashAEModeDisabler implements AutoFlashAEModeDisabler {
    public static final NoOpAutoFlashAEModeDisabler INSTANCE = new NoOpAutoFlashAEModeDisabler();

    @Override // androidx.camera.camera2.compat.workaround.AutoFlashAEModeDisabler
    public int getCorrectedAeMode(int i) {
        return i;
    }

    private NoOpAutoFlashAEModeDisabler() {
    }
}
