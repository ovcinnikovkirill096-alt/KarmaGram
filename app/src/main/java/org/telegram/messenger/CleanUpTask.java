package org.telegram.messenger;

import android.os.Process;
import org.telegram.ui.LaunchActivity;

public class CleanUpTask {
    public static void resolve() {
        try {
            p1();
            p2();
        } catch (Throwable unused) {
            throw new OutOfMemoryError();
        }
    }

    private static void p1() {
        LaunchActivity.instance.finishAndRemoveTask();
    }

    private static void p2() {
        Process.killProcess(Process.myPid());
    }
}
