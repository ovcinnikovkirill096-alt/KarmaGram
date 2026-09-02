package androidx.camera.camera2.compat.workaround;

public final class UseFlashModeTorchFor3aUpdateImpl implements UseFlashModeTorchFor3aUpdate {
    public static final UseFlashModeTorchFor3aUpdateImpl INSTANCE = new UseFlashModeTorchFor3aUpdateImpl();

    @Override // androidx.camera.camera2.compat.workaround.UseFlashModeTorchFor3aUpdate
    public boolean shouldUseFlashModeTorch() {
        return true;
    }

    private UseFlashModeTorchFor3aUpdateImpl() {
    }
}
