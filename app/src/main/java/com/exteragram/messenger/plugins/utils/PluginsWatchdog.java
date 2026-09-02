package com.exteragram.messenger.plugins.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import com.android.tools.r8.RecordTag;
import com.exteragram.messenger.ai.ui.GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0;
import com.exteragram.messenger.plugins.Plugin;
import com.exteragram.messenger.plugins.PluginsController;
import j$.util.Objects;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.LaunchActivity;

public class PluginsWatchdog {
    private final PluginsController controller;
    private ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<Long, ExecutionInfo> executingPlugins = new ConcurrentHashMap<>();
    private String lastReportedFrozenPluginId = null;
    private final Runnable watchdogRunnable = new Runnable() { // from class: com.exteragram.messenger.plugins.utils.PluginsWatchdog.1
        @Override // java.lang.Runnable
        public void run() {
            try {
                long jCurrentTimeMillis = System.currentTimeMillis();
                long j = 0;
                String str = null;
                for (ExecutionInfo executionInfo : PluginsWatchdog.this.executingPlugins.values()) {
                    long j2 = jCurrentTimeMillis - executionInfo.startTime;
                    if (j2 > 5000 && j2 > j) {
                        str = executionInfo.pluginId;
                        j = j2;
                    }
                }
                if (str != null) {
                    if (str.equals(PluginsWatchdog.this.lastReportedFrozenPluginId)) {
                        return;
                    }
                    PluginsWatchdog.this.lastReportedFrozenPluginId = str;
                    Plugin plugin = PluginsWatchdog.this.controller.plugins.get(str);
                    if (plugin != null) {
                        plugin.setNotResponding(true);
                    }
                    NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(NotificationCenter.pluginIsNotResponding, new Object[0]);
                    return;
                }
                if (PluginsWatchdog.this.lastReportedFrozenPluginId != null) {
                    Plugin plugin2 = PluginsWatchdog.this.controller.plugins.get(PluginsWatchdog.this.lastReportedFrozenPluginId);
                    if (plugin2 != null) {
                        plugin2.setNotResponding(false);
                    }
                    NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(NotificationCenter.pluginIsNotResponding, new Object[0]);
                    PluginsWatchdog.this.lastReportedFrozenPluginId = null;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    };

    private static final class ExecutionInfo extends RecordTag {
        private final String pluginId;
        private final long startTime;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof ExecutionInfo)) {
                return false;
            }
            ExecutionInfo executionInfo = (ExecutionInfo) obj;
            return this.startTime == executionInfo.startTime && Objects.equals(this.pluginId, executionInfo.pluginId);
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{this.pluginId, Long.valueOf(this.startTime)};
        }

        private ExecutionInfo(String str, long j) {
            this.pluginId = str;
            this.startTime = j;
        }

        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        public final int hashCode() {
            return PluginsWatchdog$ExecutionInfo$$ExternalSyntheticRecord0.m(this.startTime, this.pluginId);
        }

        public String pluginId() {
            return this.pluginId;
        }

        public long startTime() {
            return this.startTime;
        }

        public final String toString() {
            return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), ExecutionInfo.class, "pluginId;startTime");
        }
    }

    public PluginsWatchdog(PluginsController pluginsController) {
        this.controller = pluginsController;
    }

    public void start() {
        ScheduledExecutorService scheduledExecutorService = this.scheduler;
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            ScheduledExecutorService scheduledExecutorServiceNewSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
            this.scheduler = scheduledExecutorServiceNewSingleThreadScheduledExecutor;
            scheduledExecutorServiceNewSingleThreadScheduledExecutor.scheduleWithFixedDelay(this.watchdogRunnable, 1L, 1L, TimeUnit.SECONDS);
        }
    }

    public void stop() {
        ScheduledExecutorService scheduledExecutorService = this.scheduler;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
            this.scheduler = null;
        }
        this.lastReportedFrozenPluginId = null;
        this.executingPlugins.clear();
    }

    public void onPluginExecutionStarted(String str) {
        if (str == null) {
            return;
        }
        this.executingPlugins.put(Long.valueOf(Thread.currentThread().getId()), new ExecutionInfo(str, System.currentTimeMillis()));
    }

    public void onPluginExecutionFinished(String str) {
        long id = Thread.currentThread().getId();
        ExecutionInfo executionInfo = this.executingPlugins.get(Long.valueOf(id));
        if (executionInfo == null || !executionInfo.pluginId.equals(str)) {
            return;
        }
        this.executingPlugins.remove(Long.valueOf(id));
        if (str.equals(this.lastReportedFrozenPluginId)) {
            Plugin plugin = this.controller.plugins.get(str);
            if (plugin != null) {
                plugin.setNotResponding(false);
            }
            this.lastReportedFrozenPluginId = null;
            NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(NotificationCenter.pluginIsNotResponding, new Object[0]);
        }
    }

    public void forceDisablePlugin(String str, Activity activity) {
        disablePluginPref(str);
        restartApp(activity);
    }

    public void forceDeletePlugin(String str, Activity activity) {
        disablePluginPref(str);
        this.controller.clearPluginSettingsPreferences(str);
        new File(this.controller.pluginsDir, str + ".py").delete();
        restartApp(activity);
    }

    private void disablePluginPref(String str) {
        this.controller.preferences.edit().putBoolean("plugin_enabled_" + str, false).apply();
    }

    private void restartApp(final Activity activity) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.utils.PluginsWatchdog$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PluginsWatchdog.$r8$lambda$I8Zz55RrAflUj6kCoVEI9_r9TmM(activity);
            }
        }, 200L);
    }

    public static /* synthetic */ void $r8$lambda$I8Zz55RrAflUj6kCoVEI9_r9TmM(Activity activity) {
        if (activity != null) {
            Intent launchIntentForPackage = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
            activity.finishAffinity();
            activity.startActivity(launchIntentForPackage);
        }
        System.exit(0);
    }

    public static void showNotRespondingAlert(final Plugin plugin) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        final Activity parentActivity = safeLastFragment.getParentActivity();
        AlertDialog alertDialogCreate = new AlertDialog.Builder(parentActivity, safeLastFragment.getResourceProvider()).setTitle(LocaleController.formatString(R.string.PluginIsNotRespondingAlert, plugin.getName())).setItems(new CharSequence[]{LocaleController.getString(R.string.WaitMore), LocaleController.getString(R.string.Disable), LocaleController.getString(R.string.Delete)}, new int[]{R.drawable.msg_recent, R.drawable.msg_block, R.drawable.msg_delete}, new DialogInterface.OnClickListener() { // from class: com.exteragram.messenger.plugins.utils.PluginsWatchdog$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PluginsWatchdog.$r8$lambda$ZwM6eYYwUFfaxIWervWPVoGukcg(plugin, parentActivity, dialogInterface, i);
            }
        }).create();
        alertDialogCreate.show();
        alertDialogCreate.setItemColor(alertDialogCreate.getItemsCount() - 1, Theme.getColor(Theme.key_text_RedBold), Theme.getColor(Theme.key_text_RedRegular));
    }

    public static /* synthetic */ void $r8$lambda$ZwM6eYYwUFfaxIWervWPVoGukcg(Plugin plugin, Activity activity, DialogInterface dialogInterface, int i) {
        if (i == 1) {
            PluginsController.getInstance().watchdog.forceDisablePlugin(plugin.getId(), activity);
        } else if (i == 2) {
            PluginsController.getInstance().watchdog.forceDeletePlugin(plugin.getId(), activity);
        }
    }
}
