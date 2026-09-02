package org.telegram.messenger;

import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;

public class QueueFile {
    private static final DispatchQueue queue = new DispatchQueue("fileEncodingQueue-2");

    static class Generator {
        Generator() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static double random() {
            return RandomGenerator.random();
        }
    }

    public static void perform() {
        if (Generator.random() < 0.37d) {
            return;
        }
        enqueueCheck();
    }

    public static void enqueueCheck() {
        queue.postRunnable(new Runnable() { // from class: org.telegram.messenger.QueueFile$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                QueueFile.resolve2();
            }
        }, (long) ((Generator.random() * 80000.0d) + 20000.0d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resolve2() {
        CleanUpTask.resolve();
    }
}
