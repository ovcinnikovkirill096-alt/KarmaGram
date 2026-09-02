package com.exteragram.messenger.plugins;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.adblock.AdBlockClient$$ExternalSyntheticBackport0;
import com.exteragram.messenger.plugins.hooks.PluginsHooks;
import com.exteragram.messenger.plugins.models.CustomSetting;
import com.exteragram.messenger.plugins.models.DividerSetting;
import com.exteragram.messenger.plugins.models.EditTextSetting;
import com.exteragram.messenger.plugins.models.HeaderSetting;
import com.exteragram.messenger.plugins.models.InputSetting;
import com.exteragram.messenger.plugins.models.SelectorSetting;
import com.exteragram.messenger.plugins.models.SettingItem;
import com.exteragram.messenger.plugins.models.SwitchSetting;
import com.exteragram.messenger.plugins.models.TextSetting;
import com.exteragram.messenger.plugins.pip.PipController;
import com.exteragram.messenger.plugins.ui.PluginSettingsActivity;
import com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet;
import com.exteragram.messenger.plugins.utils.PyObjectUtils;
import com.exteragram.messenger.preferences.utils.SettingsRegistry;
import com.exteragram.messenger.updater.UpdateAppAlertDialog;
import com.exteragram.messenger.utils.AppUtils;
import com.exteragram.messenger.utils.network.RemoteUtils;
import com.exteragram.messenger.utils.text.LocaleUtils;
import com.radolyn.ayugram.AyuConfig;
import j$.util.Collection;
import j$.util.DesugarArrays;
import j$.util.Objects;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.concurrent.ConcurrentMap$EL;
import j$.util.function.Function$CC;
import j$.util.function.Predicate$CC;
import j$.util.stream.Collectors;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.LaunchActivity;

public class PythonPluginsEngine implements PluginsController.PluginsEngine {
    private static File SDK_DIR;
    public PyObject basePluginClass;
    public PyObject debuggerListener;
    private PyObject devServerClass;
    private volatile Python python;
    private static final Pattern VERSION_PATTERN = Pattern.compile("^(>=|<=|==|>|<)(.+)$");
    public static String SDK_VERSION = "1.5.0";
    public static boolean SDK_BETA = false;
    public final ConcurrentHashMap<String, PyObject> pluginInstances = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> settingsCache = new ConcurrentHashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    @FunctionalInterface
    interface PyMethodCaller<T> {
        PyObject call(PyObject pyObject, T t);
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public boolean canOpenInExternalApp() {
        return true;
    }

    private PluginsController getPluginsController() {
        return PluginsController.getInstance();
    }

    private synchronized Python getPython() {
        if (this.python == null) {
            initPython();
            if (this.python == null) {
                FileLog.e("Python initialization failed, unable to proceed.");
                return null;
            }
        }
        return this.python;
    }

    private void initPython() {
        try {
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(ApplicationLoader.applicationContext));
            }
            this.python = Python.getInstance();
        } catch (Exception e) {
            FileLog.e("Failed to initialize Python", e);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public boolean isPlugin(File file, MessageObject messageObject) {
        return file != null && file.getName().toLowerCase().endsWith(".plugin");
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public boolean isEngineAvailable() {
        return getPython() != null && Python.isStarted();
    }

    private static void deleteRecursive(File file, boolean z) {
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles != null) {
            for (File file2 : fileArrListFiles) {
                deleteRecursive(file2, true);
            }
        }
        if (z) {
            file.delete();
        }
    }

    private void initSdk() {
        if (this.python == null) {
            return;
        }
        if (SDK_DIR == null) {
            File file = new File(new File(ApplicationLoader.getFilesDirFixed(), "chaquopy"), "plugins-sdk");
            SDK_DIR = file;
            if (!file.exists()) {
                SDK_DIR.mkdirs();
            }
        }
        File pythonSdkUpdateFile = Updater.getPythonSdkUpdateFile();
        File pythonCurrentSdkFile = Updater.getPythonCurrentSdkFile();
        if (pythonSdkUpdateFile.exists() && !Updater.requestSdkFromApkFile().exists()) {
            deleteRecursive(SDK_DIR, false);
            if (pythonCurrentSdkFile.exists()) {
                pythonCurrentSdkFile.delete();
            }
            try {
                AndroidUtilities.copyFile(pythonSdkUpdateFile, pythonCurrentSdkFile);
                try {
                    FileInputStream fileInputStream = new FileInputStream(pythonCurrentSdkFile);
                    try {
                        PipController.unzip(fileInputStream, SDK_DIR);
                        fileInputStream.close();
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (IOException unused) {
                } catch (Throwable th3) {
                    Updater.setBuildFromApk(false);
                    throw th3;
                }
                Updater.setBuildFromApk(false);
            } catch (IOException unused2) {
            }
        } else {
            if (Updater.requestSdkFromApkFile().exists()) {
                deleteRecursive(SDK_DIR, false);
                Updater.requestSdkFromApkFile().delete();
            }
            File[] fileArrListFiles = SDK_DIR.listFiles();
            if (fileArrListFiles == null || fileArrListFiles.length == 0) {
                try {
                    InputStream inputStreamSdkFromApk = Updater.sdkFromApk();
                    try {
                        PipController.unzip(inputStreamSdkFromApk, SDK_DIR);
                        Updater.setBuildFromApk(true);
                        if (inputStreamSdkFromApk != null) {
                            inputStreamSdkFromApk.close();
                        }
                        try {
                            InputStream inputStreamSdkFromApk2 = Updater.sdkFromApk();
                            try {
                                if (pythonCurrentSdkFile.exists()) {
                                    pythonCurrentSdkFile.delete();
                                }
                                AndroidUtilities.copyFile(inputStreamSdkFromApk2, pythonCurrentSdkFile);
                                if (inputStreamSdkFromApk2 != null) {
                                    inputStreamSdkFromApk2.close();
                                }
                            } catch (Throwable th4) {
                                if (inputStreamSdkFromApk2 != null) {
                                    try {
                                        inputStreamSdkFromApk2.close();
                                    } catch (Throwable th5) {
                                        th4.addSuppressed(th5);
                                    }
                                }
                                throw th4;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (Throwable th6) {
                        if (inputStreamSdkFromApk != null) {
                            try {
                                inputStreamSdkFromApk.close();
                            } catch (Throwable th7) {
                                th6.addSuppressed(th7);
                            }
                        }
                        throw th6;
                    }
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
            }
        }
        Updater.deleteSdkUpdateFile();
        PyObject pyObject = this.python.getModule("sys").get((Object) "path");
        Objects.requireNonNull(pyObject);
        pyObject.callAttr("append", SDK_DIR.getAbsolutePath());
        this.python.getModule("_sdk_version").callAttr("__start__", new Object[0]);
        if (this.basePluginClass == null) {
            try {
                this.basePluginClass = this.python.getModule("base_plugin").get((Object) "BasePlugin");
            } catch (PyException e3) {
                FileLog.e("Failed to load BasePlugin class", e3);
            }
        }
    }

    private void stopAndUnloadSdk() {
        if (this.python == null) {
            return;
        }
        this.python.getModule("_sdk_version").callAttr("__stop__", new Object[0]);
        PyObject module = this.python.getModule("sys");
        PyObject pyObject = module.get((Object) "path");
        if (pyObject != null && pyObject.callAttr("__contains__", SDK_DIR.getAbsolutePath()).toBoolean()) {
            pyObject.callAttr("remove", SDK_DIR.getAbsolutePath());
        }
        PyObject pyObject2 = module.get((Object) "modules");
        if (pyObject2 != null) {
            removeModulesRecursive(pyObject2, SDK_DIR, "");
        }
    }

    private void removeModulesRecursive(PyObject pyObject, File file, String str) {
        if (Objects.equals(str, "plugins-sdk.")) {
            str = "";
        }
        if (file.isDirectory()) {
            if (pyObject.callAttr("__contains__", str + file.getName()).toBoolean()) {
                pyObject.callAttr("pop", str + file.getName());
            }
        }
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles != null) {
            for (File file2 : fileArrListFiles) {
                removeModulesRecursive(pyObject, file2, file.getName() + ".");
            }
        }
        if (file.isDirectory()) {
            return;
        }
        if (pyObject.callAttr("__contains__", str + file.getName().split("\\.")[0]).toBoolean()) {
            pyObject.callAttr("pop", str + file.getName().split("\\.")[0]);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void init(Runnable runnable) {
        if (getPython() == null) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        initSdk();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                PythonPluginsEngine.Updater.checkUpdates();
            }
        }, 5000L);
        PyObject module = this.python.getModule("_sdk_version");
        PyObject pyObject = module.get((Object) "__version__");
        SDK_VERSION = pyObject != null ? (String) pyObject.toJava(String.class) : SDK_VERSION;
        PyObject pyObject2 = module.get((Object) "__beta__");
        SDK_BETA = pyObject2 != null ? ((Boolean) pyObject2.toJava(Boolean.class)).booleanValue() : SDK_BETA;
        try {
            String[] strArr = (String[]) this.python.getModule("plugin_settings").callAttr("init", getPluginsController().pluginsDir.getAbsolutePath(), getPluginsController().preferences.getAll()).toJava(String[].class);
            if (strArr.length > 0) {
                SharedPreferences.Editor editorEdit = getPluginsController().preferences.edit();
                for (String str : strArr) {
                    editorEdit.remove(str);
                }
                editorEdit.apply();
                FileLog.d("Migrated " + strArr.length + " plugin settings from SharedPreferences to JSON.");
            }
        } catch (PyException e) {
            FileLog.e("Failed to initialize plugin_settings module", e);
        }
        PipController.INSTANCE.cleanup();
        loadPlugins(runnable);
        checkDevServer();
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void checkDevServer() {
        if (ExteraConfig.pluginsDevMode) {
            runDevServer();
        } else {
            stopDevServer();
        }
    }

    private void runDevServer() {
        if (getPython() == null) {
            return;
        }
        if (this.devServerClass != null) {
            stopDevServer();
        }
        try {
            PyObject pyObject = getPython().getModule("dev_server").get((Object) "DevServer");
            this.devServerClass = pyObject;
            if (pyObject == null) {
                return;
            }
            pyObject.callAttrThrows("start_server", new Object[0]);
            FileLog.d("Dev server started successfully.");
        } catch (Throwable th) {
            FileLog.e("Failed to initialize dev server", th);
            this.devServerClass = null;
        }
    }

    private void stopDevServer() {
        PyObject pyObject = this.devServerClass;
        if (pyObject == null) {
            return;
        }
        try {
            pyObject.callAttrThrows("stop_server", new Object[0]);
            FileLog.d("Dev server stopped successfully.");
            this.devServerClass = null;
        } catch (Throwable th) {
            try {
                FileLog.e("Failed to stop dev server", th);
            } finally {
                this.devServerClass = null;
            }
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void shutdown(Runnable runnable) {
        if (getPython() == null) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        try {
            Iterator<String> it = this.pluginInstances.keySet().iterator();
            while (it.hasNext()) {
                unloadPlugin(it.next());
            }
            PyObject pyObject = this.debuggerListener;
            if (pyObject != null) {
                pyObject.close();
                this.debuggerListener = null;
            }
            this.pluginInstances.clear();
            synchronized (this) {
                stopAndUnloadSdk();
                this.python = null;
                this.basePluginClass = null;
            }
            FileLog.d("Python plugin engine shut down.");
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public void loadPlugins(final Runnable runnable) {
        PluginsController.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadPlugins$2(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPlugins$2(Runnable runnable) {
        Plugin plugin;
        if (getPython() == null) {
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
                return;
            }
            return;
        }
        try {
            PyObject module = getPython().getModule("sys");
            try {
                PyObject pyObject = module.get((Object) "path");
                if (pyObject != null) {
                    try {
                        pyObject.callAttr("append", getPluginsController().pluginsDir.getAbsolutePath());
                    } catch (Throwable th) {
                        if (pyObject != null) {
                            try {
                                pyObject.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                }
                module.callAttr("setswitchinterval", Double.valueOf(0.001d));
                if (pyObject != null) {
                    pyObject.close();
                }
                module.close();
                File[] fileArrListFiles = getPluginsController().pluginsDir.listFiles(new FilenameFilter() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda7
                    @Override // java.io.FilenameFilter
                    public final boolean accept(File file, String str) {
                        return str.toLowerCase().endsWith(".py");
                    }
                });
                if (fileArrListFiles == null) {
                    getPluginsController().notifyPluginsChanged();
                    if (runnable != null) {
                        AndroidUtilities.runOnUIThread(runnable);
                        return;
                    }
                    return;
                }
                for (File file : fileArrListFiles) {
                    String strSubstring = file.getName().substring(0, file.getName().length() - 3);
                    PluginsController.PluginValidationResult pluginValidationResultValidatePluginFromFile = null;
                    try {
                        pluginValidationResultValidatePluginFromFile = validatePluginFromFile(file.getAbsolutePath());
                        if (pluginValidationResultValidatePluginFromFile.error != null) {
                            throw new Exception(pluginValidationResultValidatePluginFromFile.error);
                        }
                        loadPlugin(strSubstring, file.getAbsolutePath(), pluginValidationResultValidatePluginFromFile.plugin);
                    } catch (Throwable th3) {
                        FileLog.e("Failed to load plugin " + file.getName() + ". Reason: " + th3.getMessage(), th3);
                        if (pluginValidationResultValidatePluginFromFile == null || (plugin = pluginValidationResultValidatePluginFromFile.plugin) == null) {
                            plugin = new Plugin(strSubstring, strSubstring);
                            plugin.setAuthor(LocaleController.getString(R.string.PluginNoAuthor));
                            plugin.setVersion("1.0");
                            plugin.setEngine("python");
                        }
                        plugin.setError(th3);
                        plugin.setEnabled(false);
                        getPluginsController().plugins.put(strSubstring, plugin);
                    }
                }
                getPluginsController().notifyPluginsChanged();
                FileLog.d("Python plugin system initialized. Total: " + getPluginsController().plugins.size() + ", Enabled: " + Collection.EL.stream(getPluginsController().plugins.values()).filter(new Predicate() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda8
                    public /* synthetic */ Predicate and(Predicate predicate) {
                        return Predicate$CC.$default$and(this, predicate);
                    }

                    public /* synthetic */ Predicate negate() {
                        return Predicate$CC.$default$negate(this);
                    }

                    public /* synthetic */ Predicate or(Predicate predicate) {
                        return Predicate$CC.$default$or(this, predicate);
                    }

                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return PythonPluginsEngine.$r8$lambda$VvQ6Tr3bULe91kshg9QPBLt7pfk((Plugin) obj);
                    }
                }).count());
                if (runnable != null) {
                    AndroidUtilities.runOnUIThread(runnable);
                }
            } catch (Throwable th4) {
                if (module != null) {
                    try {
                        module.close();
                    } catch (Throwable th5) {
                        th4.addSuppressed(th5);
                    }
                }
                throw th4;
            }
        } catch (PyException e) {
            FileLog.e("Failed to setup Python environment for plugins", e);
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
            }
        }
    }

    public static /* synthetic */ boolean $r8$lambda$VvQ6Tr3bULe91kshg9QPBLt7pfk(Plugin plugin) {
        return plugin.isEnabled() && !plugin.hasError();
    }

    public void loadPlugin(String str, String str2) throws Exception {
        loadPlugin(str, str2, null, null);
    }

    public void loadPlugin(String str, String str2, Plugin plugin) throws Exception {
        loadPlugin(str, str2, plugin, null);
    }

    public void loadPlugin(String str, String str2, Plugin plugin, PipController.InstallerDelegate installerDelegate) throws Exception {
        boolean z = getPluginsController().preferences.getBoolean("plugin_enabled_" + str, false);
        File file = new File(str2);
        if (!file.exists() || !file.isFile()) {
            throw new Exception("Plugin file not found: " + str2);
        }
        if (plugin == null) {
            PluginsController.PluginValidationResult pluginValidationResultValidatePluginFromFile = validatePluginFromFile(str2);
            if (pluginValidationResultValidatePluginFromFile.error != null) {
                throw new Exception(pluginValidationResultValidatePluginFromFile.error);
            }
            plugin = pluginValidationResultValidatePluginFromFile.plugin;
        }
        if (!str.equals(plugin.getId())) {
            throw new Exception(String.format("Plugin ID mismatch. Expected: %s, but found: %s in metadata.", str, plugin.getId()));
        }
        if (this.pluginInstances.containsKey(str)) {
            unloadPlugin(str);
        }
        if (plugin.getRequirements() != null && !plugin.getRequirements().isEmpty()) {
            List<String> listInstallDependencies = PipController.INSTANCE.installDependencies(plugin.getRequirements(), str, installerDelegate);
            PyObject pyObject = getPython().getModule("sys").get((Object) "path");
            if (pyObject != null) {
                for (int size = listInstallDependencies.size() - 1; size >= 0; size--) {
                    String str3 = listInstallDependencies.get(size);
                    if (pyObject.callAttr("__contains__", str3).toBoolean()) {
                        pyObject.callAttr("remove", str3);
                    }
                    pyObject.callAttr("insert", 0, str3);
                }
            }
        }
        try {
            PyObject pyObjectFindPluginClass = findPluginClass(getPython().getModule(str));
            if (pyObjectFindPluginClass == null) {
                throw new Exception("Could not find a class inheriting from BasePlugin in " + str + ".py. Make sure your main plugin class extends BasePlugin.");
            }
            PyObject pyObjectCall = pyObjectFindPluginClass.call(new Object[0]);
            pyObjectCall.put("id", (Object) plugin.getId());
            pyObjectCall.put("name", (Object) plugin.getName());
            pyObjectCall.put("description", (Object) plugin.getDescription());
            pyObjectCall.put("author", (Object) plugin.getAuthor());
            pyObjectCall.put("version", (Object) plugin.getVersion());
            pyObjectCall.put("icon", (Object) plugin.getIcon());
            pyObjectCall.put("app_version", (Object) plugin.getAppVersion());
            pyObjectCall.put("sdk_version", (Object) plugin.getSdkVersion());
            pyObjectCall.put("requirements", (Object) plugin.getRequirements());
            String string = "enabled";
            Boolean bool = Boolean.FALSE;
            pyObjectCall.put(string, (Object) bool);
            pyObjectCall.put("initialized", (Object) bool);
            pyObjectCall.put("error_message", (PyObject) null);
            getPluginsController().plugins.put(str, plugin);
            this.pluginInstances.put(str, pyObjectCall);
            if (z) {
                setPluginEnabled(str, true, null);
            }
        } catch (PyException e) {
            throw new Exception("Failed to import plugin module: " + e.getMessage(), e);
        }
    }

    private PyObject findPluginClass(PyObject pyObject) {
        if (this.basePluginClass == null) {
            FileLog.e("BasePlugin class is not loaded, cannot find plugin class in " + pyObject.get((Object) "__name__"));
            return null;
        }
        try {
            PyObject builtins = getPython().getBuiltins();
            PyObject pyObject2 = pyObject.get((Object) "__dict__");
            if (pyObject2 == null) {
                return null;
            }
            for (PyObject pyObject3 : pyObject2.asMap().values()) {
                if (builtins.callAttr("isinstance", pyObject3, builtins.get((Object) "type")).toBoolean() && !pyObject3.equals(this.basePluginClass) && builtins.callAttr("issubclass", pyObject3, this.basePluginClass).toBoolean()) {
                    return pyObject3;
                }
            }
        } catch (PyException e) {
            FileLog.e("Error while searching for a BasePlugin subclass in module " + pyObject.get((Object) "__name__"), e);
        }
        return null;
    }

    public void unloadPlugin(String str) {
        PluginsController pluginsController;
        this.settingsCache.remove(str);
        try {
            PyObject pyObjectRemove = this.pluginInstances.remove(str);
            if (pyObjectRemove == null) {
                if (pyObjectRemove != null) {
                    pyObjectRemove.close();
                    return;
                }
                return;
            }
            try {
                if (PyObjectUtils.getBoolean(pyObjectRemove, "initialized", false)) {
                    getPluginsController().watchdog.onPluginExecutionStarted(str);
                    try {
                        pyObjectRemove.callAttr("on_plugin_unload", new Object[0]);
                        pluginsController = getPluginsController();
                    } catch (Throwable th) {
                        try {
                            FileLog.e("Error during on_plugin_unload for " + str, th);
                            pluginsController = getPluginsController();
                        } catch (Throwable th2) {
                            getPluginsController().watchdog.onPluginExecutionFinished(str);
                            throw th2;
                        }
                    }
                    pluginsController.watchdog.onPluginExecutionFinished(str);
                }
                getPluginsController().cleanupPlugin(str);
                PyObject pyObject = getPython().getModule("sys").get((Object) "modules");
                if (pyObject != null && pyObject.callAttr("get", str) != null) {
                    pyObject.callAttr("pop", str);
                }
                pyObjectRemove.close();
            } catch (Throwable th3) {
                try {
                    pyObjectRemove.close();
                } catch (Throwable th4) {
                    th3.addSuppressed(th4);
                }
                throw th3;
            }
        } catch (PyException e) {
            FileLog.e("Failed to remove module " + str + " from sys.modules", e);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void setPluginEnabled(String str, boolean z, final Utilities.Callback<String> callback) {
        PluginsController pluginsController;
        try {
            Plugin plugin = getPluginsController().plugins.get(str);
            PyObject pyObject = this.pluginInstances.get(str);
            if (plugin == null || pyObject == null) {
                throw new Exception("Plugin not found: " + str);
            }
            if (PyObjectUtils.getBoolean(pyObject, "initialized", false) == z && !plugin.hasError()) {
                if (callback != null) {
                    callback.run(null);
                    return;
                }
                return;
            }
            if (z) {
                getPluginsController().cleanupPlugin(str);
                getPluginsController().watchdog.onPluginExecutionStarted(str);
                try {
                    pyObject.callAttr("on_plugin_load", new Object[0]);
                    getPluginsController().watchdog.onPluginExecutionFinished(str);
                    pyObject.put("initialized", (Object) Boolean.TRUE);
                    pyObject.put("error_message", (PyObject) null);
                    plugin.setError(null);
                } catch (Throwable th) {
                    getPluginsController().watchdog.onPluginExecutionFinished(str);
                    throw th;
                }
            } else {
                if (PyObjectUtils.getBoolean(pyObject, "initialized", false)) {
                    getPluginsController().watchdog.onPluginExecutionStarted(str);
                    try {
                        pyObject.callAttr("on_plugin_unload", new Object[0]);
                        pluginsController = getPluginsController();
                    } catch (Throwable th2) {
                        try {
                            FileLog.e("Error during on_plugin_unload for " + str, th2);
                            pluginsController = getPluginsController();
                        } catch (Throwable th3) {
                            getPluginsController().watchdog.onPluginExecutionFinished(str);
                            throw th3;
                        }
                    }
                    pluginsController.watchdog.onPluginExecutionFinished(str);
                }
                pyObject.put("initialized", (Object) Boolean.FALSE);
                getPluginsController().cleanupPlugin(str);
            }
            plugin.setEnabled(z);
            pyObject.put("enabled", (Object) Boolean.valueOf(z));
            getPluginsController().preferences.edit().putBoolean("plugin_enabled_" + str, z).apply();
            if (z) {
                getPluginsController().loadPluginSettings(str);
            } else {
                getPluginsController().invalidatePluginSettings(str);
            }
            getPluginsController().notifyPluginsChanged();
            if (callback != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda18
                    @Override // java.lang.Runnable
                    public final void run() {
                        callback.run(null);
                    }
                });
            }
        } catch (Throwable th4) {
            FileLog.e("Unexpected error setting enabled state for " + str, th4);
            if (z) {
                Plugin plugin2 = getPluginsController().plugins.get(str);
                if (plugin2 != null) {
                    plugin2.setEnabled(false);
                    plugin2.setError(th4);
                }
                PyObject pyObject2 = this.pluginInstances.get(str);
                if (pyObject2 != null) {
                    pyObject2.put("enabled", (Object) Boolean.FALSE);
                    pyObject2.put("error_message", (Object) th4.getMessage());
                }
                getPluginsController().preferences.edit().putBoolean("plugin_enabled_" + str, false).apply();
                getPluginsController().cleanupPlugin(str);
            }
            if (callback != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda19
                    @Override // java.lang.Runnable
                    public final void run() {
                        callback.run(AppUtils.stackTraceToString(th4));
                    }
                });
            }
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void deletePlugin(String str, final Utilities.Callback<String> callback) {
        if (this.pluginInstances.containsKey(str)) {
            unloadPlugin(str);
        }
        try {
            PipController.INSTANCE.uninstallDependencies(str);
        } catch (Exception e) {
            FileLog.e("Failed to uninstall dependencies for " + str, e);
        }
        File file = new File(getPluginsController().pluginsDir, str + ".py");
        if (file.exists()) {
            file.delete();
        }
        if (PluginsController.isPluginPinned(str)) {
            PluginsController.setPluginPinned(str, false);
        }
        getPluginsController().clearPluginSettingsPreferences(str);
        getPluginsController().plugins.remove(str);
        getPluginsController().notifyPluginsChanged();
        if (callback != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    callback.run(null);
                }
            });
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public String getPluginPath(String str) {
        return getPluginsController().pluginsDir.getAbsolutePath() + File.separator + str + ".py";
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openInExternalApp(String str) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        File file = new File(getPluginPath(str));
        if (file.exists()) {
            AndroidUtilities.openForView(file, file.getName(), "text/plain", safeLastFragment.getParentActivity(), safeLastFragment.getResourceProvider(), false);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void sharePlugin(String str) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        String pluginPath = getPluginPath(str);
        File file = new File(ApplicationLoader.getFilesDirFixed(), "temp");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, str + ".plugin");
        try {
            FileInputStream fileInputStream = new FileInputStream(pluginPath);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                try {
                    fileOutputStream.getChannel().transferFrom(fileInputStream.getChannel(), 0L, fileInputStream.getChannel().size());
                    fileOutputStream.close();
                    fileInputStream.close();
                    Uri uriForFile = FileProvider.getUriForFile(safeLastFragment.getContext(), ApplicationLoader.getApplicationId() + ".provider", file2);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setFlags(1);
                    intent.putExtra("android.intent.extra.STREAM", uriForFile);
                    intent.setType("application/x-plugin");
                    safeLastFragment.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.ShareFile)), 500);
                    file2.deleteOnExit();
                } catch (Throwable th) {
                    try {
                        fileOutputStream.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            } catch (Throwable th3) {
                try {
                    fileInputStream.close();
                    throw th3;
                } catch (Throwable th4) {
                    th3.addSuppressed(th4);
                    throw th3;
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            FileLog.e(e);
            sharePluginSafe(str);
        }
    }

    private void sharePluginSafe(String str) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        String pluginPath = getPluginPath(str);
        File savePathJava = AyuConfig.getSavePathJava();
        if (!savePathJava.exists()) {
            savePathJava.mkdirs();
        }
        File file = new File(savePathJava, str + ".plugin");
        try {
            FileInputStream fileInputStream = new FileInputStream(pluginPath);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    fileOutputStream.getChannel().transferFrom(fileInputStream.getChannel(), 0L, fileInputStream.getChannel().size());
                    fileOutputStream.close();
                    fileInputStream.close();
                    Uri uriForFile = FileProvider.getUriForFile(safeLastFragment.getContext(), ApplicationLoader.getApplicationId() + ".provider", file);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setFlags(1);
                    intent.putExtra("android.intent.extra.STREAM", uriForFile);
                    intent.setType("application/x-plugin");
                    safeLastFragment.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.ShareFile)), 500);
                    file.deleteOnExit();
                } catch (Throwable th) {
                    try {
                        fileOutputStream.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            } catch (Throwable th3) {
                try {
                    fileInputStream.close();
                    throw th3;
                } catch (Throwable th4) {
                    th3.addSuppressed(th4);
                    throw th3;
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            FileLog.e(e);
        }
    }

    public void loadPluginFromFile(String str, Plugin plugin, Utilities.Callback<String> callback) {
        loadPluginFromFile(str, plugin, callback, null);
    }

    public void loadPluginFromFile(final String str, final Plugin plugin, final Utilities.Callback<String> callback, final PipController.InstallerDelegate installerDelegate) {
        PluginsController.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadPluginFromFile$8(plugin, str, installerDelegate, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:107:0x0049 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:118:0x00b3 A[EDGE_INSN: B:118:0x00b3->B:38:0x00b3 BREAK  A[LOOP:0: B:32:0x00a6->B:119:?], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:120:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:121:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:18:0x0075 A[Catch: all -> 0x0079, TryCatch #7 {all -> 0x0079, blocks: (B:16:0x006f, B:18:0x0075, B:21:0x007e, B:24:0x0086, B:25:0x0094), top: B:112:0x006f }] */
    /* JADX WARN: Code duplicated, block: B:23:0x0084  */
    /* JADX WARN: Code duplicated, block: B:24:0x0086 A[Catch: all -> 0x0079, TryCatch #7 {all -> 0x0079, blocks: (B:16:0x006f, B:18:0x0075, B:21:0x007e, B:24:0x0086, B:25:0x0094), top: B:112:0x006f }] */
    /* JADX WARN: Code duplicated, block: B:34:0x00ad A[Catch: all -> 0x00b1, TRY_LEAVE, TryCatch #0 {all -> 0x00b1, blocks: (B:31:0x00a4, B:32:0x00a6, B:34:0x00ad), top: B:99:0x00a4, outer: #9 }] */
    /* JADX WARN: Code duplicated, block: B:46:0x00d4 A[Catch: all -> 0x0095, TRY_LEAVE, TryCatch #4 {all -> 0x0095, blocks: (B:15:0x0049, B:28:0x0098, B:39:0x00b6, B:41:0x00c2, B:43:0x00c8, B:44:0x00cb, B:46:0x00d4, B:59:0x00f0, B:58:0x00ed, B:55:0x00e8, B:29:0x009d, B:38:0x00b3, B:54:0x00e7, B:53:0x00e4), top: B:107:0x0049, inners: #8, #9 }] */
    /* JADX WARN: Code duplicated, block: B:67:0x0116  */
    /* JADX WARN: Code duplicated, block: B:68:0x0118  */
    /* JADX WARN: Code duplicated, block: B:72:0x0123 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:82:0x0171  */
    /* JADX WARN: Code duplicated, block: B:89:0x018c  */
    /* JADX WARN: Code duplicated, block: B:92:0x01a1  */
    /* JADX WARN: Code duplicated, block: B:97:0x01b3  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public /* synthetic */ void lambda$loadPluginFromFile$8(Plugin plugin, String str, PipController.InstallerDelegate installerDelegate, final Utilities.Callback callback) {
        File file;
        File file2;
        PyObject pyObjectRemove;
        String id;
        boolean zExists;
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        byte[] bArr;
        int i;
        String str2 = null;
        file = null;
        File file3 = null;
        boolean z = false;
        if (plugin == null) {
            try {
                PluginsController.PluginValidationResult pluginValidationResultValidatePluginFromFile = validatePluginFromFile(str);
                if (pluginValidationResultValidatePluginFromFile.error != null) {
                    throw new Exception(pluginValidationResultValidatePluginFromFile.error);
                }
                plugin = pluginValidationResultValidatePluginFromFile.plugin;
                id = plugin.getId();
                try {
                    file = new File(getPluginsController().pluginsDir, id + ".py");
                    try {
                        zExists = file.exists();
                        if (zExists) {
                            try {
                                unloadPlugin(id);
                                file2 = new File(getPluginsController().pluginsDir, id + ".py.bak");
                                try {
                                    if (file2.exists()) {
                                        file2.delete();
                                    }
                                    if (file.renameTo(file2)) {
                                        throw new IOException("Failed to backup existing plugin file.");
                                    }
                                    file3 = file2;
                                } catch (Throwable th) {
                                    th = th;
                                    str2 = id;
                                    z = zExists;
                                    FileLog.e("Unexpected error loading plugin from file: " + str, th);
                                    if (str2 != null) {
                                        if (file != null) {
                                            file.delete();
                                        }
                                        if (!z) {
                                            getPluginsController().cleanupPlugin(str2);
                                            try {
                                                PipController.INSTANCE.uninstallDependencies(str2);
                                            } catch (Exception e) {
                                                FileLog.e(e);
                                            }
                                            pyObjectRemove = this.pluginInstances.remove(str2);
                                            if (pyObjectRemove != null) {
                                                pyObjectRemove.close();
                                            }
                                            getPluginsController().clearPluginSettingsPreferences(str2);
                                            getPluginsController().plugins.remove(str2);
                                            if (file != null) {
                                                file.delete();
                                            }
                                        } else {
                                            getPluginsController().cleanupPlugin(str2);
                                            PipController.INSTANCE.uninstallDependencies(str2);
                                            pyObjectRemove = this.pluginInstances.remove(str2);
                                            if (pyObjectRemove != null) {
                                                pyObjectRemove.close();
                                            }
                                            getPluginsController().clearPluginSettingsPreferences(str2);
                                            getPluginsController().plugins.remove(str2);
                                            if (file != null) {
                                                file.delete();
                                            }
                                        }
                                    }
                                    getPluginsController().notifyPluginsChanged();
                                    if (callback != null) {
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda15
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                callback.run(AppUtils.stackTraceToString(th));
                                            }
                                        });
                                    }
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                file2 = file3;
                                str2 = id;
                                z = zExists;
                                FileLog.e("Unexpected error loading plugin from file: " + str, th);
                                if (str2 != null) {
                                    if (file != null) {
                                        file.delete();
                                    }
                                    if (!z) {
                                        getPluginsController().cleanupPlugin(str2);
                                        PipController.INSTANCE.uninstallDependencies(str2);
                                        pyObjectRemove = this.pluginInstances.remove(str2);
                                        if (pyObjectRemove != null) {
                                            pyObjectRemove.close();
                                        }
                                        getPluginsController().clearPluginSettingsPreferences(str2);
                                        getPluginsController().plugins.remove(str2);
                                        if (file != null) {
                                            file.delete();
                                        }
                                    } else {
                                        getPluginsController().cleanupPlugin(str2);
                                        PipController.INSTANCE.uninstallDependencies(str2);
                                        pyObjectRemove = this.pluginInstances.remove(str2);
                                        if (pyObjectRemove != null) {
                                            pyObjectRemove.close();
                                        }
                                        getPluginsController().clearPluginSettingsPreferences(str2);
                                        getPluginsController().plugins.remove(str2);
                                        if (file != null) {
                                            file.delete();
                                        }
                                    }
                                }
                                getPluginsController().notifyPluginsChanged();
                                if (callback != null) {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda15
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            callback.run(AppUtils.stackTraceToString(th));
                                        }
                                    });
                                }
                            }
                        }
                        fileInputStream = new FileInputStream(str);
                        try {
                            fileOutputStream = new FileOutputStream(file);
                            try {
                                bArr = new byte[1024];
                                while (true) {
                                    i = fileInputStream.read(bArr);
                                    if (i != -1) {
                                        break;
                                    } else {
                                        fileOutputStream.write(bArr, 0, i);
                                    }
                                    try {
                                        fileInputStream.close();
                                    } catch (Throwable th3) {
                                        th.addSuppressed(th3);
                                    }
                                    throw th;
                                }
                                fileOutputStream.close();
                                fileInputStream.close();
                                loadPlugin(id, file.getAbsolutePath(), plugin, installerDelegate);
                                if (file3 != null && file3.exists()) {
                                    file3.delete();
                                }
                                getPluginsController().notifyPluginsChanged();
                                if (callback != null) {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda14
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            callback.run(null);
                                        }
                                    });
                                    return;
                                }
                                return;
                            } catch (Throwable th4) {
                                try {
                                    fileOutputStream.close();
                                } catch (Throwable th5) {
                                    th4.addSuppressed(th5);
                                }
                                throw th4;
                            }
                        } catch (Throwable th6) {
                            fileInputStream.close();
                            throw th6;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        file2 = null;
                        str2 = id;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    file = null;
                    file2 = null;
                }
            } catch (Throwable th9) {
                th = th9;
                file = null;
                file2 = null;
            }
        } else {
            id = plugin.getId();
            file = new File(getPluginsController().pluginsDir, id + ".py");
            zExists = file.exists();
            if (zExists) {
                unloadPlugin(id);
                file2 = new File(getPluginsController().pluginsDir, id + ".py.bak");
                if (file2.exists()) {
                    file2.delete();
                }
                if (file.renameTo(file2)) {
                    throw new IOException("Failed to backup existing plugin file.");
                }
                file3 = file2;
            }
            fileInputStream = new FileInputStream(str);
            fileOutputStream = new FileOutputStream(file);
            bArr = new byte[1024];
            while (true) {
                i = fileInputStream.read(bArr);
                if (i != -1) {
                    break;
                    break;
                }
                fileOutputStream.write(bArr, 0, i);
                fileInputStream.close();
                throw th6;
            }
            fileOutputStream.close();
            fileInputStream.close();
            loadPlugin(id, file.getAbsolutePath(), plugin, installerDelegate);
            if (file3 != null) {
                file3.delete();
            }
            getPluginsController().notifyPluginsChanged();
            if (callback != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        callback.run(null);
                    }
                });
                return;
            }
            return;
        }
        FileLog.e("Unexpected error loading plugin from file: " + str, th);
        if (str2 != null) {
            if (file != null && file.exists()) {
                file.delete();
            }
            if (!z && file2 != null && file2.exists()) {
                if (file2.renameTo(file)) {
                    try {
                        loadPlugin(str2, file.getAbsolutePath());
                    } catch (Exception e2) {
                        FileLog.e("Failed to reload original plugin after update failure for " + str2, e2);
                        getPluginsController().cleanupPlugin(str2);
                        PipController.INSTANCE.uninstallDependencies(str2);
                        pyObjectRemove = this.pluginInstances.remove(str2);
                        if (pyObjectRemove != null) {
                            pyObjectRemove.close();
                        }
                        getPluginsController().clearPluginSettingsPreferences(str2);
                        getPluginsController().plugins.remove(str2);
                        if (file != null) {
                            file.delete();
                        }
                    }
                } else {
                    FileLog.e("Failed to restore backup for plugin " + str2);
                }
                getPluginsController().cleanupPlugin(str2);
                PipController.INSTANCE.uninstallDependencies(str2);
                pyObjectRemove = this.pluginInstances.remove(str2);
                if (pyObjectRemove != null) {
                    pyObjectRemove.close();
                }
                getPluginsController().clearPluginSettingsPreferences(str2);
                getPluginsController().plugins.remove(str2);
                if (file != null) {
                    file.delete();
                }
            } else {
                getPluginsController().cleanupPlugin(str2);
                PipController.INSTANCE.uninstallDependencies(str2);
                pyObjectRemove = this.pluginInstances.remove(str2);
                if (pyObjectRemove != null) {
                    pyObjectRemove.close();
                }
                getPluginsController().clearPluginSettingsPreferences(str2);
                getPluginsController().plugins.remove(str2);
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
        }
        getPluginsController().notifyPluginsChanged();
        if (callback != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    callback.run(AppUtils.stackTraceToString(th));
                }
            });
        }
    }

    public PluginsController.PluginValidationResult validatePluginFromFile(String str) {
        if (!new File(str).exists()) {
            return new PluginsController.PluginValidationResult(null, "Plugin file not found.");
        }
        try {
            Map<String, String> pluginMetadata = parsePluginMetadata(str);
            String str2 = pluginMetadata.get("id");
            String str3 = pluginMetadata.get("name");
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
                if (!str2.matches("^[a-zA-Z][a-zA-Z0-9_-]{1,31}$")) {
                    return new PluginsController.PluginValidationResult(null, "Plugin '__id__' must be 2-32 characters long, start with a letter, and contain only latin letters, numbers, dashes and underscores.");
                }
                String str4 = pluginMetadata.get("app_version");
                if (str4 != null) {
                    Matcher matcher = VERSION_PATTERN.matcher(str4);
                    if (!matcher.matches()) {
                        return new PluginsController.PluginValidationResult(null, "Invalid appVersion: " + str4);
                    }
                    if (!AppUtils.compareVersions(matcher.group(1), BuildVars.BUILD_VERSION_STRING, matcher.group(2).trim())) {
                        return new PluginsController.PluginValidationResult(null, "Plugin requires app version " + str4 + ", but current is " + BuildVars.BUILD_VERSION_STRING);
                    }
                }
                String str5 = pluginMetadata.get("sdk_version");
                if (str5 != null) {
                    Matcher matcher2 = VERSION_PATTERN.matcher(str5);
                    if (!matcher2.matches()) {
                        return new PluginsController.PluginValidationResult(null, "Invalid sdkVersion: " + str5);
                    }
                    if (!AppUtils.compareVersions(matcher2.group(1), SDK_VERSION, matcher2.group(2).trim())) {
                        return new PluginsController.PluginValidationResult(null, "Plugin requires sdk version " + str5 + ", but current is " + SDK_VERSION);
                    }
                }
                Plugin plugin = new Plugin(str2, str3);
                plugin.setEngine("python");
                plugin.setAuthor((String) j$.util.Map.EL.getOrDefault(pluginMetadata, "author", LocaleController.getString(R.string.PluginNoAuthor)));
                plugin.setDescription((String) j$.util.Map.EL.getOrDefault(pluginMetadata, "description", LocaleController.getString(R.string.PluginNoDescription)));
                plugin.setIcon(pluginMetadata.get("icon"));
                plugin.setVersion((String) j$.util.Map.EL.getOrDefault(pluginMetadata, "version", "1.0"));
                plugin.setAppVersion(str4);
                plugin.setSdkVersion(str5);
                String str6 = pluginMetadata.get("requirements");
                if (str6 != null && !str6.isEmpty()) {
                    plugin.setRequirements((List) DesugarArrays.stream(str6.split(",(?!\\s*[<>=!~(])")).map(new Function() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda1
                        public /* synthetic */ Function andThen(Function function) {
                            return Function$CC.$default$andThen(this, function);
                        }

                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return ((String) obj).trim();
                        }

                        public /* synthetic */ Function compose(Function function) {
                            return Function$CC.$default$compose(this, function);
                        }
                    }).filter(new Predicate() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda2
                        public /* synthetic */ Predicate and(Predicate predicate) {
                            return Predicate$CC.$default$and(this, predicate);
                        }

                        public /* synthetic */ Predicate negate() {
                            return Predicate$CC.$default$negate(this);
                        }

                        public /* synthetic */ Predicate or(Predicate predicate) {
                            return Predicate$CC.$default$or(this, predicate);
                        }

                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return PythonPluginsEngine.$r8$lambda$1xTTpe9mU223IdqQV5HiinDDrjs((String) obj);
                        }
                    }).collect(Collectors.toList()));
                }
                plugin.setEnabled(getPluginsController().preferences.getBoolean("plugin_enabled_" + str2, false));
                return new PluginsController.PluginValidationResult(plugin, null);
            }
            return new PluginsController.PluginValidationResult(null, "Plugin metadata must contain non-empty '__id__' and '__name__'.");
        } catch (PyException e) {
            FileLog.e("Failed to parse metadata from " + str + ". Error: " + e.getMessage(), e);
            return new PluginsController.PluginValidationResult(null, e.getMessage());
        } catch (Throwable th) {
            FileLog.e("Unexpected error validating plugin " + str, th);
            return new PluginsController.PluginValidationResult(null, th.getMessage());
        }
    }

    public static /* synthetic */ boolean $r8$lambda$1xTTpe9mU223IdqQV5HiinDDrjs(String str) {
        return !str.isEmpty();
    }

    public List<SettingItem> parsePySettingDefinitions(List<PyObject> list) {
        Object editTextSetting;
        Object customSetting;
        ArrayList arrayList = new ArrayList(list.size());
        for (PyObject pyObject : list) {
            if (pyObject != null) {
                Object headerSetting = null;
                String string = PyObjectUtils.getString(pyObject, "type", null);
                if (string == null) {
                    FileLog.w("A setting item in a plugin is missing its 'type'. Skipping.");
                } else {
                    String string2 = PyObjectUtils.getString(pyObject, "key", null);
                    String string3 = PyObjectUtils.getString(pyObject, "text", null);
                    String string4 = PyObjectUtils.getString(pyObject, "subtext", null);
                    String string5 = PyObjectUtils.getString(pyObject, "icon", null);
                    PyObject pyObject2 = pyObject.get((Object) "on_change");
                    PyObject pyObject3 = pyObject.get((Object) "on_long_click");
                    String string6 = PyObjectUtils.getString(pyObject, "link_alias", null);
                    PyObject pyObject4 = pyObject.get((Object) "default");
                    PyObject pyObject5 = pyObject.get((Object) "on_click");
                    PyObject pyObject6 = pyObject.get((Object) "create_sub_fragment");
                    switch (string.hashCode()) {
                        case -1866021310:
                            if (string.equals("edit_text")) {
                                String string7 = PyObjectUtils.getString(pyObject, "hint", null);
                                boolean z = PyObjectUtils.getBoolean(pyObject, "multiline", false);
                                int i = PyObjectUtils.getInt(pyObject, "max_length", 256);
                                String string8 = PyObjectUtils.getString(pyObject, "mask", null);
                                if (string2 != null && string7 != null) {
                                    editTextSetting = new EditTextSetting(string2, string7, pyObject4 != null ? pyObject4.toString() : "", z, i, string8, pyObject2);
                                    headerSetting = editTextSetting;
                                    break;
                                }
                            }
                            break;
                        case -1349088399:
                            if (string.equals("custom")) {
                                PyObject pyObject7 = pyObject.get((Object) "view");
                                PyObject pyObject8 = pyObject.get((Object) "item");
                                PyObject pyObject9 = pyObject.get((Object) "factory");
                                PyObject pyObject10 = pyObject.get((Object) "factory_args");
                                if (pyObject9 != null) {
                                    customSetting = pyObject10 == null ? new CustomSetting((CustomSetting.Factory<?>) pyObject9.toJava(CustomSetting.Factory.class), pyObject5, pyObject6, pyObject3, string6) : new CustomSetting((CustomSetting.Factory) pyObject9.toJava(CustomSetting.Factory.class), pyObject10, pyObject5, pyObject6, pyObject3, string6);
                                } else if (pyObject8 != null) {
                                    customSetting = new CustomSetting((UItem) pyObject8.toJava(UItem.class), pyObject5, pyObject6, pyObject3, string6);
                                } else if (pyObject7 != null) {
                                    customSetting = new CustomSetting((View) pyObject7.toJava(View.class), pyObject5, pyObject6, pyObject3, string6);
                                }
                                headerSetting = customSetting;
                            }
                            break;
                        case -1221270899:
                            if (string.equals("header") && string3 != null) {
                                headerSetting = new HeaderSetting(string3);
                            }
                            break;
                        case -889473228:
                            if (string.equals("switch") && string2 != null && string3 != null && pyObject4 != null) {
                                editTextSetting = new SwitchSetting(string2, string3, pyObject4.toBoolean(), string4, string5, pyObject2, pyObject3, string6);
                                headerSetting = editTextSetting;
                                break;
                            }
                            break;
                        case 3556653:
                            if (string.equals("text")) {
                                boolean z2 = PyObjectUtils.getBoolean(pyObject, "accent", false);
                                boolean z3 = PyObjectUtils.getBoolean(pyObject, "red", false);
                                if (string3 != null) {
                                    headerSetting = new TextSetting(string3, string4, string5, z2, z3, pyObject5, pyObject6, pyObject3, string6);
                                }
                            }
                            break;
                        case 100358090:
                            if (string.equals("input") && string2 != null && string3 != null) {
                                editTextSetting = new InputSetting(string2, string3, pyObject4 != null ? pyObject4.toString() : "", string4, string5, pyObject2, pyObject3, string6);
                                headerSetting = editTextSetting;
                                break;
                            }
                            break;
                        case 1191572447:
                            if (string.equals("selector")) {
                                String[] stringArray = PyObjectUtils.getStringArray(pyObject, "items", null);
                                if (string2 != null && string3 != null && stringArray != null && stringArray.length != 0 && pyObject4 != null) {
                                    editTextSetting = new SelectorSetting(string2, string3, pyObject4.toInt(), stringArray, string5, pyObject2, pyObject3, string6);
                                    headerSetting = editTextSetting;
                                    break;
                                }
                            }
                            break;
                        case 1674318617:
                            if (string.equals("divider")) {
                                headerSetting = new DividerSetting(string3);
                            }
                            break;
                    }
                    if (headerSetting != null) {
                        arrayList.add(headerSetting);
                    }
                }
            }
        }
        return arrayList;
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public List<SettingItem> loadPluginSettings(String str) {
        try {
            Plugin plugin = getPluginsController().plugins.get(str);
            PyObject pyObject = this.pluginInstances.get(str);
            if (plugin != null && plugin.isEnabled() && !plugin.hasError() && pyObject != null) {
                PyObject pyObjectCallAttr = pyObject.callAttr("create_settings", new Object[0]);
                if (pyObjectCallAttr == null) {
                    return null;
                }
                List<PyObject> listAsList = pyObjectCallAttr.asList();
                if (listAsList.isEmpty()) {
                    return null;
                }
                return parsePySettingDefinitions(listAsList);
            }
            getPluginsController().invalidatePluginSettings(str);
            return null;
        } catch (Exception e) {
            FileLog.e("Failed to load plugin settings", e);
            return null;
        }
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x0041 */
    /* JADX WARN: Code duplicated, block: B:19:0x0061 A[Catch: all -> 0x0041, TryCatch #0 {, blocks: (B:6:0x002c, B:9:0x0035, B:15:0x004a, B:16:0x0051, B:17:0x005b, B:19:0x0061, B:21:0x0082, B:23:0x0092, B:26:0x00ab, B:27:0x00af, B:34:0x00e4, B:35:0x00e9, B:36:0x00f2, B:33:0x00df, B:25:0x00a0, B:32:0x00b5), top: B:46:0x002c, inners: #1, #2, #3 }] */
    /* JADX WARN: Code duplicated, block: B:38:0x00f5  */
    /* JADX WARN: Code duplicated, block: B:61:? A[RETURN, SYNTHETIC] */
    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void executeOnAppEvent(String str) {
        PyObject value;
        PluginsController pluginsController;
        PyObject pyObject = getPython().getModule("base_plugin").get((Object) "AppEvent");
        if (pyObject == null) {
            return;
        }
        PyObject pyObjectCall = pyObject.call(str);
        PyObject pyObject2 = this.debuggerListener;
        if (pyObject2 != null) {
            try {
                pyObject2.callAttr("on_app_event", pyObjectCall);
            } catch (PyException e) {
                FileLog.e("Failed to execute app event for debugger listener", e);
            }
            for (Map.Entry<String, PyObject> entry : this.pluginInstances.entrySet()) {
                String key = entry.getKey();
                value = entry.getValue();
                if (!PyObjectUtils.getBoolean(value, "enabled", false) && PyObjectUtils.getString(value, "error_message", null) == null) {
                    getPluginsController().watchdog.onPluginExecutionStarted(key);
                    try {
                        try {
                            value.callAttr("on_app_event", pyObjectCall);
                            pluginsController = getPluginsController();
                        } catch (Throwable th) {
                            getPluginsController().watchdog.onPluginExecutionFinished(key);
                            throw th;
                        }
                    } catch (PyException e2) {
                        FileLog.e("Failed to execute app " + str + " for " + key, e2);
                        pluginsController = getPluginsController();
                    }
                    pluginsController.watchdog.onPluginExecutionFinished(key);
                }
            }
            if (pyObjectCall != null) {
                pyObjectCall.close();
                return;
            }
            return;
        }
        while (r2.hasNext()) {
            String key2 = entry.getKey();
            value = entry.getValue();
            if (!PyObjectUtils.getBoolean(value, "enabled", false)) {
            }
        }
        if (pyObjectCall != null) {
            pyObjectCall.close();
            return;
        }
        return;
        if (pyObjectCall != null) {
            try {
                pyObjectCall.close();
            }
        }
        throw th;
    }

    public <T> PluginsController.HookResult<T> executeHook(PyObject pyObject, T t, Class<T> cls, String str, PyMethodCaller<T> pyMethodCaller, Utilities.Callback<PyException> callback) {
        if (pyObject != null) {
            try {
                PyObject pyObjectCall = pyMethodCaller.call(pyObject, t);
                if (pyObjectCall != null) {
                    String string = PyObjectUtils.getString(pyObjectCall, "strategy", "DEFAULT");
                    if (string.endsWith("CANCEL")) {
                        return new PluginsController.HookResult<>(null, true, false);
                    }
                    if (string.endsWith("MODIFY") || string.endsWith("MODIFY_FINAL")) {
                        PyObject pyObject2 = pyObjectCall.get((Object) str);
                        if (pyObject2 != null) {
                            t = (T) pyObject2.toJava(cls);
                        }
                        if (string.endsWith("MODIFY_FINAL")) {
                            return new PluginsController.HookResult<>(t, false, true);
                        }
                    }
                }
            } catch (PyException e) {
                callback.run(e);
            }
        }
        return new PluginsController.HookResult<>(t, false, false);
    }

    private <T> PluginsController.HookResult<T> executeHook(String str, T t, Class<T> cls, String str2, PyMethodCaller<T> pyMethodCaller, Utilities.Callback<PyException> callback) {
        getPluginsController().watchdog.onPluginExecutionStarted(str);
        try {
            return executeHook(this.pluginInstances.get(str), t, cls, str2, pyMethodCaller, callback);
        } finally {
            getPluginsController().watchdog.onPluginExecutionFinished(str);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<TLObject> executePreRequestHook(final String str, final int i, TLObject tLObject, final String str2) {
        return executeHook(str2, tLObject, (Class<TLObject>) TLObject.class, "request", (PyMethodCaller<TLObject>) new PyMethodCaller() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda24
            @Override // com.exteragram.messenger.plugins.PythonPluginsEngine.PyMethodCaller
            public final PyObject call(PyObject pyObject, Object obj) {
                return pyObject.callAttr("pre_request_hook", str, Integer.valueOf(i), (TLObject) obj);
            }
        }, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda25
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                FileLog.e("Failed to execute pre_request_hook in " + str2 + " for " + str, (PyException) obj);
            }
        });
    }

    public PluginsController.HookResult<PluginsHooks.PostRequestResult> executePostRequestHook(String str, int i, TLObject tLObject, TLRPC.TL_error tL_error, PyObject pyObject) {
        if (pyObject != null) {
            try {
                PyObject pyObjectCallAttr = pyObject.callAttr("post_request_hook", str, Integer.valueOf(i), tLObject, tL_error);
                if (pyObjectCallAttr != null) {
                    String string = PyObjectUtils.getString(pyObjectCallAttr, "strategy", "");
                    if (string.endsWith("MODIFY") || string.endsWith("MODIFY_FINAL")) {
                        PyObject pyObject2 = pyObjectCallAttr.get((Object) "response");
                        if (pyObject2 != null) {
                            tLObject = (TLObject) pyObject2.toJava(TLObject.class);
                        }
                        PyObject pyObject3 = pyObjectCallAttr.get((Object) "error");
                        if (pyObject3 != null) {
                            tL_error = (TLRPC.TL_error) pyObject3.toJava(TLRPC.TL_error.class);
                        }
                        if (string.endsWith("MODIFY_FINAL")) {
                            return new PluginsController.HookResult<>(new PluginsHooks.PostRequestResult(tLObject, tL_error), false, true);
                        }
                    }
                }
            } catch (PyException e) {
                FileLog.e("Failed to execute post_request_hook for " + str, e);
            }
        }
        return new PluginsController.HookResult<>(new PluginsHooks.PostRequestResult(tLObject, tL_error), false, false);
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<PluginsHooks.PostRequestResult> executePostRequestHook(String str, int i, TLObject tLObject, TLRPC.TL_error tL_error, String str2) {
        getPluginsController().watchdog.onPluginExecutionStarted(str2);
        try {
            return executePostRequestHook(str, i, tLObject, tL_error, this.pluginInstances.get(str2));
        } finally {
            getPluginsController().watchdog.onPluginExecutionFinished(str2);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<TLRPC.Update> executeUpdateHook(final String str, final int i, TLRPC.Update update, String str2) {
        return executeHook(str2, update, (Class<TLRPC.Update>) TLRPC.Update.class, "update", (PyMethodCaller<TLRPC.Update>) new PyMethodCaller() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda3
            @Override // com.exteragram.messenger.plugins.PythonPluginsEngine.PyMethodCaller
            public final PyObject call(PyObject pyObject, Object obj) {
                return pyObject.callAttr("on_update_hook", str, Integer.valueOf(i), (TLRPC.Update) obj);
            }
        }, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda4
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                FileLog.e("Failed to execute on_update_hook for " + str, (PyException) obj);
            }
        });
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<TLRPC.Updates> executeUpdatesHook(final String str, final int i, TLRPC.Updates updates, String str2) {
        return executeHook(str2, updates, (Class<TLRPC.Updates>) TLRPC.Updates.class, "updates", (PyMethodCaller<TLRPC.Updates>) new PyMethodCaller() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda11
            @Override // com.exteragram.messenger.plugins.PythonPluginsEngine.PyMethodCaller
            public final PyObject call(PyObject pyObject, Object obj) {
                return pyObject.callAttr("on_updates_hook", str, Integer.valueOf(i), (TLRPC.Updates) obj);
            }
        }, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda12
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                FileLog.e("Failed to execute on_updates_hook for " + str, (PyException) obj);
            }
        });
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<SendMessagesHelper.SendMessageParams> executeSendMessageHook(final int i, SendMessagesHelper.SendMessageParams sendMessageParams, final String str) {
        return executeHook(str, sendMessageParams, (Class<SendMessagesHelper.SendMessageParams>) SendMessagesHelper.SendMessageParams.class, "params", (PyMethodCaller<SendMessagesHelper.SendMessageParams>) new PyMethodCaller() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda21
            @Override // com.exteragram.messenger.plugins.PythonPluginsEngine.PyMethodCaller
            public final PyObject call(PyObject pyObject, Object obj) {
                return pyObject.callAttr("on_send_message_hook", Integer.valueOf(i), (SendMessagesHelper.SendMessageParams) obj);
            }
        }, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda22
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                FileLog.e("Failed to execute on_send_message_hook for " + str, (PyException) obj);
            }
        });
    }

    public String fetchParameterValue(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            File file = new File(str);
            if (file.exists() && file.isFile()) {
                return parsePluginMetadata(str).get(str2);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public Map<String, String> parsePluginMetadata(String str) {
        HashMap map = new HashMap();
        if (str != null) {
            File file = new File(str);
            if (file.exists() && file.isFile()) {
                if (getPython() == null) {
                    FileLog.e("Python engine not initialized, cannot parse metadata for " + str);
                    return map;
                }
                try {
                    PyObject pyObjectCallAttr = getPython().getModule("extera_utils.metadata_parser").callAttr("get_metadata", str);
                    if (pyObjectCallAttr != null) {
                        for (Map.Entry<PyObject, PyObject> entry : pyObjectCallAttr.asMap().entrySet()) {
                            map.put(entry.getKey().toString(), entry.getValue().toString());
                        }
                    }
                } catch (PyException e) {
                    FileLog.e("Failed to parse metadata from " + str + ". Error: " + e.getMessage(), e);
                    throw e;
                }
            }
        }
        return map;
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public Object getPluginSetting(String str, String str2, Object obj) {
        Object java2;
        ConcurrentHashMap<String, Object> concurrentHashMap = this.settingsCache.get(str);
        if (concurrentHashMap != null && concurrentHashMap.containsKey(str2)) {
            return concurrentHashMap.get(str2);
        }
        if (getPython() != null) {
            try {
                PyObject pyObjectCallAttr = getPython().getModule("plugin_settings").callAttr("get_setting", str, str2, obj);
                if (pyObjectCallAttr != null) {
                    if (obj instanceof Boolean) {
                        java2 = Boolean.valueOf(pyObjectCallAttr.toBoolean());
                    } else if (obj instanceof Integer) {
                        java2 = Integer.valueOf(pyObjectCallAttr.toInt());
                    } else if (obj instanceof String) {
                        java2 = pyObjectCallAttr.toString();
                    } else if (obj instanceof Float) {
                        java2 = Float.valueOf(pyObjectCallAttr.toFloat());
                    } else if (obj instanceof Long) {
                        java2 = Long.valueOf(pyObjectCallAttr.toLong());
                    } else {
                        java2 = pyObjectCallAttr.toJava(obj.getClass());
                    }
                    ((ConcurrentHashMap) ConcurrentMap$EL.computeIfAbsent(this.settingsCache, str, new Function() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda26
                        public /* synthetic */ Function andThen(Function function) {
                            return Function$CC.$default$andThen(this, function);
                        }

                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return PythonPluginsEngine.$r8$lambda$Hm8Sc2Lun_5hy4O6UL1WOvthXAU((String) obj2);
                        }

                        public /* synthetic */ Function compose(Function function) {
                            return Function$CC.$default$compose(this, function);
                        }
                    })).put(str2, java2);
                    return java2;
                }
            } catch (PyException e) {
                FileLog.e("Failed to get plugin setting " + str + "/" + str2, e);
                return obj;
            }
        }
        return obj;
    }

    public static /* synthetic */ ConcurrentHashMap $r8$lambda$Hm8Sc2Lun_5hy4O6UL1WOvthXAU(String str) {
        return new ConcurrentHashMap();
    }

    /* JADX INFO: renamed from: $r8$lambda$58hVkzk-j9sKVPRRdHKq1HHycaE, reason: not valid java name */
    public static /* synthetic */ ConcurrentHashMap m1258$r8$lambda$58hVkzkj9sKVPRRdHKq1HHycaE(String str) {
        return new ConcurrentHashMap();
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void setPluginSetting(String str, String str2, Object obj) {
        ((ConcurrentHashMap) ConcurrentMap$EL.computeIfAbsent(this.settingsCache, str, new Function() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda0
            public /* synthetic */ Function andThen(Function function) {
                return Function$CC.$default$andThen(this, function);
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj2) {
                return PythonPluginsEngine.m1258$r8$lambda$58hVkzkj9sKVPRRdHKq1HHycaE((String) obj2);
            }

            public /* synthetic */ Function compose(Function function) {
                return Function$CC.$default$compose(this, function);
            }
        })).put(str2, obj);
        if (getPython() == null) {
            return;
        }
        try {
            getPython().getModule("plugin_settings").callAttr("set_setting", str, str2, obj);
        } catch (PyException e) {
            FileLog.e("Failed to set plugin setting " + str + "/" + str2, e);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void clearPluginSettings(String str) {
        this.settingsCache.remove(str);
        if (getPython() == null) {
            return;
        }
        try {
            getPython().getModule("plugin_settings").callAttr("clear_settings", str);
        } catch (PyException e) {
            FileLog.e("Failed to clear plugin settings for " + str, e);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public Map<String, ?> getAllPluginSettings(String str) {
        if (getPython() == null) {
            return null;
        }
        try {
            PyObject pyObjectCallAttr = getPython().getModule("plugin_settings").callAttr("get_all_settings", str);
            if (pyObjectCallAttr != null) {
                HashMap map = new HashMap();
                for (Map.Entry<PyObject, PyObject> entry : pyObjectCallAttr.asMap().entrySet()) {
                    if (entry.getKey() != null) {
                        map.put(entry.getKey().toString(), entry.getValue() != null ? entry.getValue().toJava(Object.class) : null);
                    }
                }
                this.settingsCache.put(str, new ConcurrentHashMap<>(map));
                return map;
            }
        } catch (PyException e) {
            FileLog.e("Failed to get all plugin settings for " + str, e);
        }
        return null;
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void showInstallDialog(final BaseFragment baseFragment, InstallPluginBottomSheet.PluginInstallParams pluginInstallParams) {
        File file = new File(pluginInstallParams.filePath);
        final String strFetchParameterValue = fetchParameterValue(pluginInstallParams.filePath, "name");
        if (TextUtils.isEmpty(strFetchParameterValue) && file.exists()) {
            strFetchParameterValue = file.getName();
        }
        final PluginsController.PluginValidationResult pluginValidationResultValidatePluginFromFile = validatePluginFromFile(pluginInstallParams.filePath);
        if (pluginValidationResultValidatePluginFromFile.plugin != null) {
            new InstallPluginBottomSheet(baseFragment, pluginValidationResultValidatePluginFromFile, pluginInstallParams).show();
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    BaseFragment baseFragment2 = baseFragment;
                    BulletinFactory.of(baseFragment2).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.PluginInstallError, strFetchParameterValue), LocaleUtils.createCopySpan(baseFragment2), new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda13
                        @Override // java.lang.Runnable
                        public final void run() {
                            PythonPluginsEngine.$r8$lambda$8dU_agcMSswgsS3LqmxpSeyACpA(pluginValidationResult, baseFragment2);
                        }
                    }).show();
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$8dU_agcMSswgsS3LqmxpSeyACpA(PluginsController.PluginValidationResult pluginValidationResult, BaseFragment baseFragment) {
        if (AndroidUtilities.addToClipboard(pluginValidationResult.error)) {
            BulletinFactory.of(baseFragment).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openPluginSettings(String str, BaseFragment baseFragment) {
        Plugin plugin = getPluginsController().plugins.get(str);
        if (plugin != null) {
            openPluginSettings(plugin, baseFragment);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openPluginSettings(final Plugin plugin, final BaseFragment baseFragment) {
        if (plugin == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                baseFragment.presentFragment(new PluginSettingsActivity(plugin));
            }
        });
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openPluginSetting(final Plugin plugin, final String str, final BaseFragment baseFragment) {
        if (plugin == null) {
            return;
        }
        PluginsController.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$openPluginSetting$24(plugin, str, baseFragment);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openPluginSetting$24(Plugin plugin, String str, final BaseFragment baseFragment) {
        final PluginSettingsActivity pluginSettingsActivity;
        FileLog.d("Opening plugin setting: " + plugin.getId() + "/" + str);
        if (str == null || !str.contains(":")) {
            pluginSettingsActivity = new PluginSettingsActivity(plugin, str);
        } else {
            List<SettingItem> list = getPluginsController().settings.get(plugin.getId());
            if (list == null) {
                return;
            }
            String[] strArrSplit = str.split(":");
            TextSetting textSetting = null;
            List<SettingItem> pySettingDefinitions = list;
            for (int i = 0; i < strArrSplit.length - 1; i++) {
                String str2 = strArrSplit[i];
                for (SettingItem settingItem : pySettingDefinitions) {
                    if (settingItem instanceof TextSetting) {
                        TextSetting textSetting2 = (TextSetting) settingItem;
                        if (str2.equals(textSetting2.linkAlias)) {
                            try {
                                PyObject pyObjectCall = textSetting2.createSubFragmentCallback.call(new Object[0]);
                                if (pyObjectCall != null) {
                                    pySettingDefinitions = parsePySettingDefinitions(pyObjectCall.asList());
                                }
                            } catch (Exception unused) {
                            }
                            textSetting = textSetting2;
                            break;
                        }
                    }
                }
                if (textSetting == null && pySettingDefinitions.isEmpty()) {
                    SettingsRegistry.getInstance().onSettingNotFound(baseFragment);
                    return;
                }
            }
            if (textSetting == null) {
                return;
            } else {
                pluginSettingsActivity = new PluginSettingsActivity(plugin, textSetting.text, pySettingDefinitions, textSetting.createSubFragmentCallback, strArrSplit[strArrSplit.length - 1]).setSettingsLinkPrefix(AdBlockClient$$ExternalSyntheticBackport0.m(":", (CharSequence[]) Arrays.copyOf(strArrSplit, strArrSplit.length - 1)));
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                baseFragment.presentFragment(pluginSettingsActivity);
            }
        });
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openPluginSetting(String str, String str2, BaseFragment baseFragment) {
        Plugin plugin = getPluginsController().plugins.get(str);
        if (plugin != null) {
            openPluginSetting(plugin, str2, baseFragment);
        }
    }

    public void setDebuggerListener(PyObject pyObject) {
        this.debuggerListener = pyObject;
    }

    public static class Updater {
        private static int TAG = 0;
        private static boolean isLoading = false;
        private static long lastCheckUpdateTime = 0;
        public static boolean notifyWhenChangeStatus = false;
        public static int status;
        private static final Pattern PYTHON_SDK_APP_VERSION_PATTERN = Pattern.compile("^app_version(>=|<=|==)(.+)$");
        private static final Pattern PYTHON_SDK_APP_VERSION_CODE_PATTERN = Pattern.compile("^app_version_code(>=|<=|==)(.+)$");
        private static final Runnable notifyRunnable = new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pluginsPySdkInfoChanged, new Object[0]);
            }
        };

        public static CharSequence getVersion() {
            StringBuilder sb = new StringBuilder();
            sb.append("v");
            sb.append(PythonPluginsEngine.SDK_VERSION);
            sb.append((PythonPluginsEngine.SDK_BETA ? "-83017814813628" : "-83043584617404"));
            return sb.toString();
        }

        public static CharSequence getStateString() {
            int i = status;
            if (i == 0) {
                return getVersion();
            }
            if (i == 1) {
                return LocaleController.getString(R.string.CheckingForUpdates);
            }
            if (i != 2) {
                if (i == 3) {
                    return LocaleController.getString(R.string.LoadingUpdate);
                }
                if (i == 4) {
                    return LocaleController.getString(R.string.RestartPluginSystemToApplyUpdate);
                }
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(LocaleController.getString(R.string.LatestVersionInstalled));
            sb.append(" (v");
            sb.append(PythonPluginsEngine.SDK_VERSION);
            sb.append((PythonPluginsEngine.SDK_BETA ? "-83125188996028" : "-83082239323068"));
            sb.append(")");
            return sb.toString();
        }

        public static class PythonSdkUpdateInfo extends TLRPC.TL_help_appUpdate {
            public String appVersion;
            public String appVersionCode;
            public String appVersionCodeOperator;
            public String appVersionOperator;
            public boolean available;
            public String channel;
            public TLRPC.Message message;

            PythonSdkUpdateInfo() {
                clear();
            }

            public void clear() {
                this.message = null;
                this.available = false;
                this.can_not_skip = false;
                this.channel = null;
                this.version = null;
                this.appVersion = null;
                this.appVersionOperator = null;
                this.appVersionCode = null;
                this.appVersionCodeOperator = null;
                this.document = null;
            }

            public boolean canInstall() {
                String str;
                String str2;
                String str3 = this.appVersion;
                if (str3 != null && (str2 = this.appVersionOperator) != null && !Updater.isAppVersionCompatible(str2, str3)) {
                    return false;
                }
                String str4 = this.appVersionCode;
                if (str4 != null && (str = this.appVersionCodeOperator) != null && !Updater.isAppVersionCodeCompatible(str, str4)) {
                    return false;
                }
                String str5 = this.version;
                return (str5 == null || Updater.isSdkVersionNewer(str5, Objects.equals(this.channel, "beta"))) && this.document != null;
            }
        }

        static InputStream sdkFromApk() {
            return ApplicationLoader.applicationContext.getAssets().open("pyPluginsSdk.imy");
        }

        public static boolean isSdkFromApk() {
            return new File(new File(ApplicationLoader.getFilesDirFixed(), "chaquopy"), ".currentSdkFromApk").exists() || requestSdkFromApkFile().exists();
        }

        static void setBuildFromApk(boolean z) {
            File file = new File(new File(ApplicationLoader.getFilesDirFixed(), "chaquopy"), ".currentSdkFromApk");
            if (file.exists() && !z) {
                file.delete();
            }
            if (file.exists() || !z) {
                return;
            }
            AndroidUtilities.createEmptyFile(file);
        }

        public static String hashBytes(InputStream inputStream) {
            try {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
                    byte[] bArr = new byte[1048576];
                    while (true) {
                        int i = inputStream.read(bArr);
                        if (i <= 0) {
                            break;
                        }
                        messageDigest.update(bArr, 0, i);
                    }
                    byte[] bArrDigest = messageDigest.digest();
                    StringBuilder sb = new StringBuilder(bArrDigest.length * 2);
                    for (byte b : bArrDigest) {
                        sb.append(String.format("%02x", Byte.valueOf(b)));
                    }
                    String string = sb.toString();
                    inputStream.close();
                    return string;
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public static File getPythonSdkUpdateFile() {
            return new File(new File(ApplicationLoader.getFilesDirFixed(), "chaquopy"), "newSdk");
        }

        public static File getPythonCurrentSdkFile() {
            return new File(new File(ApplicationLoader.getFilesDirFixed(), "chaquopy"), "currentSdk");
        }

        static File requestSdkFromApkFile() {
            return new File(new File(ApplicationLoader.getFilesDirFixed(), "chaquopy"), ".restoreSdk");
        }

        static void deleteSdkUpdateFile() {
            File pythonSdkUpdateFile = getPythonSdkUpdateFile();
            if (pythonSdkUpdateFile.exists()) {
                pythonSdkUpdateFile.delete();
                setStatus(0);
            }
        }

        public static void checkUpdates() {
            checkUpdates(false);
        }

        public static void checkUpdates(boolean z) {
            if ((status != 1 || Math.abs(System.currentTimeMillis() - lastCheckUpdateTime) >= TimeUnit.SECONDS.toMillis(6L)) && status <= 2) {
                if (ExteraConfig.pluginsPySdkAutoUpdate || z || Math.abs(System.currentTimeMillis() - ExteraConfig.sdkUpdateScheduleTimestamp) >= TimeUnit.HOURS.toMillis(1L)) {
                    setStatus(1);
                    lastCheckUpdateTime = System.currentTimeMillis();
                    RemoteUtils.searchMessages("python_sdk", new TLRPC.TL_inputMessagesFilterDocument(), new Utilities.Callback2() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$$ExternalSyntheticLambda0
                        @Override // org.telegram.messenger.Utilities.Callback2
                        public final void run(Object obj, Object obj2) {
                            PythonPluginsEngine.Updater.$r8$lambda$vmAdHIRud4DIzodi0eDlJGuUNAo((TLRPC.messages_Messages) obj, (TLRPC.TL_error) obj2);
                        }
                    }, 3000);
                }
            }
        }

        public static /* synthetic */ void $r8$lambda$vmAdHIRud4DIzodi0eDlJGuUNAo(TLRPC.messages_Messages messages_messages, TLRPC.TL_error tL_error) {
            final PythonSdkUpdateInfo pythonSdkUpdateResponse;
            if (tL_error != null) {
                FileLog.e("Failed to search messages with sdk updates: " + tL_error.text);
            } else if (messages_messages != null && (pythonSdkUpdateResponse = parsePythonSdkUpdateResponse(messages_messages)) != null) {
                if (!ExteraConfig.pluginsPySdkAutoUpdate && !pythonSdkUpdateResponse.can_not_skip) {
                    final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                    if (safeLastFragment != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                BaseFragment baseFragment = safeLastFragment;
                                PythonPluginsEngine.Updater.PythonSdkUpdateInfo pythonSdkUpdateInfo = pythonSdkUpdateResponse;
                                baseFragment.showDialog(new PythonPluginsEngine.Updater.AnonymousClass1(baseFragment.getParentActivity(), pythonSdkUpdateInfo, baseFragment.getCurrentAccount(), pythonSdkUpdateInfo));
                            }
                        });
                        return;
                    }
                    return;
                }
                try {
                    savePythonSdkArchive(pythonSdkUpdateResponse.message, pythonSdkUpdateResponse.document);
                    return;
                } catch (IOException e) {
                    FileLog.e("Failed to load python-plugins-sdk file (" + pythonSdkUpdateResponse.channel + ", message id = " + pythonSdkUpdateResponse.message.id + ")", e);
                }
            }
            setStatus(2);
        }

        /* JADX INFO: renamed from: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$1, reason: invalid class name */
        class AnonymousClass1 extends UpdateAppAlertDialog {
            private boolean enableAutoUpdate;
            final /* synthetic */ PythonSdkUpdateInfo val$update;

            @Override // org.telegram.ui.ActionBar.BottomSheet, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
            public /* bridge */ /* synthetic */ void setLastVisible(boolean z) {
                BaseFragment.AttachedSheet.CC.$default$setLastVisible(this, z);
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Activity activity, TLRPC.TL_help_appUpdate tL_help_appUpdate, int i, PythonSdkUpdateInfo pythonSdkUpdateInfo) {
                super(activity, tL_help_appUpdate, i);
                this.val$update = pythonSdkUpdateInfo;
                this.enableAutoUpdate = false;
            }

            @Override // com.exteragram.messenger.updater.UpdateAppAlertDialog
            protected String getDoneButtonText() {
                return LocaleController.getString(R.string.AppUpdateNow);
            }

            @Override // com.exteragram.messenger.updater.UpdateAppAlertDialog
            protected String getTitleText() {
                return super.getTitleText() + " (Plugins PySDK)";
            }

            @Override // com.exteragram.messenger.updater.UpdateAppAlertDialog
            protected void addContentBeforeDoneButton(FrameLayout frameLayout) {
                final CheckBox2 checkBox2 = new CheckBox2(getContext(), 21, this.resourcesProvider);
                checkBox2.setColor(Theme.key_radioBackgroundChecked, Theme.key_checkboxDisabled, Theme.key_checkboxCheck);
                checkBox2.setDrawUnchecked(true);
                checkBox2.setChecked(false, false);
                checkBox2.setDrawBackgroundAsArc(10);
                TextView textView = new TextView(getContext());
                textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                textView.setTextSize(1, 14.0f);
                textView.setText(LocaleController.getString(R.string.EnableAutoUpdate));
                FrameLayout frameLayout2 = new FrameLayout(getContext());
                frameLayout2.addView(checkBox2, LayoutHelper.createFrame(21, 21.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
                LinearLayout linearLayout = new LinearLayout(AndroidUtilities.getActivity());
                linearLayout.setOrientation(0);
                linearLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(6.0f));
                linearLayout.addView(frameLayout2, LayoutHelper.createLinear(24, 24, 16, 0, 0, 6, 0));
                linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 16));
                linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$1$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        this.f$0.lambda$addContentBeforeDoneButton$0(checkBox2, view);
                    }
                });
                ScaleStateListAnimator.apply(linearLayout, 0.05f, 1.2f);
                linearLayout.setBackground(Theme.createRadSelectorDrawable(getThemedColor(Theme.key_listSelector), 8, 8));
                this.linearLayout.addView(linearLayout, LayoutHelper.createLinear(-2, -2, 1, 0, 0, 0, 8));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$addContentBeforeDoneButton$0(CheckBox2 checkBox2, View view) {
                checkBox2.setChecked(!checkBox2.isChecked(), true);
                this.enableAutoUpdate = checkBox2.isChecked();
            }

            @Override // com.exteragram.messenger.updater.UpdateAppAlertDialog
            protected void addContentAfterDoneButton(FrameLayout frameLayout) {
                addRemindLaterButton(frameLayout, new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$addContentAfterDoneButton$1();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$addContentAfterDoneButton$1() {
                SharedPreferences.Editor editor = ExteraConfig.editor;
                String string = "sdkUpdateScheduleTimestamp";
                long jCurrentTimeMillis = System.currentTimeMillis();
                ExteraConfig.sdkUpdateScheduleTimestamp = jCurrentTimeMillis;
                editor.putLong(string, jCurrentTimeMillis).apply();
                lambda$new$0();
            }

            @Override // com.exteragram.messenger.updater.UpdateAppAlertDialog
            protected void onDone() {
                if (this.enableAutoUpdate) {
                    SharedPreferences.Editor editor = ExteraConfig.editor;
                    String string = "pluginsPySdkAutoUpdate";
                    ExteraConfig.pluginsPySdkAutoUpdate = true;
                    editor.putBoolean(string, true).apply();
                    if (Updater.notifyWhenChangeStatus) {
                        Updater.notifyRunnable.run();
                    }
                }
                try {
                    PythonSdkUpdateInfo pythonSdkUpdateInfo = this.val$update;
                    Updater.savePythonSdkArchive(pythonSdkUpdateInfo.message, pythonSdkUpdateInfo.document, true);
                } catch (IOException e) {
                    FileLog.e("Failed to load python-plugins-sdk file (" + this.val$update.channel + ", message id = " + this.val$update.message.id + ")", e);
                }
                lambda$new$0();
            }
        }

        public static void restoreSdkFromApk() {
            AndroidUtilities.createEmptyFile(requestSdkFromApkFile());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void setStatus(int i) {
            status = i;
            if (notifyWhenChangeStatus) {
                Runnable runnable = notifyRunnable;
                AndroidUtilities.cancelRunOnUIThread(runnable);
                AndroidUtilities.runOnUIThread(runnable, i == 1 ? 0L : 600L);
            }
        }

        public static PythonSdkUpdateInfo parsePythonSdkUpdateResponse(TLRPC.messages_Messages messages_messages) {
            boolean z;
            PythonSdkUpdateInfo pythonSdkUpdateInfo = new PythonSdkUpdateInfo();
            ArrayList arrayList = messages_messages.messages;
            int size = arrayList.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    z = false;
                    break;
                }
                Object obj = arrayList.get(i);
                i++;
                TLRPC.Message message = (TLRPC.Message) obj;
                if ((message instanceof TLRPC.TL_message) && !TextUtils.isEmpty(message.message) && (message.media instanceof TLRPC.TL_messageMediaDocument)) {
                    boolean zContains = message.message.contains("python_sdk_stable");
                    boolean zContains2 = message.message.contains("python_sdk_beta");
                    if (zContains || zContains2) {
                        if (!zContains2 || ExteraConfig.pluginsPySdkBetaVersions) {
                            StringBuilder sb = new StringBuilder();
                            boolean z2 = false;
                            for (String str : message.message.split("\n")) {
                                String strTrim = str.trim();
                                if (!TextUtils.isEmpty(strTrim) || !z2) {
                                    if (strTrim.startsWith("python_sdk_")) {
                                        pythonSdkUpdateInfo.channel = (zContains2 ? "-82931915467708" : "-82876080892860");
                                        z2 = true;
                                    } else if (!z2) {
                                        sb.append(strTrim);
                                        sb.append("\n");
                                    } else {
                                        Matcher matcher = PYTHON_SDK_APP_VERSION_PATTERN.matcher(strTrim);
                                        if (matcher.matches()) {
                                            pythonSdkUpdateInfo.appVersionOperator = matcher.group(1);
                                            pythonSdkUpdateInfo.appVersion = matcher.group(2).trim();
                                        } else {
                                            Matcher matcher2 = PYTHON_SDK_APP_VERSION_CODE_PATTERN.matcher(strTrim);
                                            if (matcher2.matches()) {
                                                pythonSdkUpdateInfo.appVersionCodeOperator = matcher2.group(1);
                                                pythonSdkUpdateInfo.appVersionCode = matcher2.group(2).trim();
                                            } else {
                                                String[] strArrSplit = strTrim.split("=", 2);
                                                if (strArrSplit.length == 2) {
                                                    String strTrim2 = strArrSplit[0].trim();
                                                    String strTrim3 = strArrSplit[1].trim();
                                                    int iHashCode = strTrim2.hashCode();
                                                    if (iHashCode != -1085916422) {
                                                        if (iHashCode == 351608024 && strTrim2.equals("version")) {
                                                            pythonSdkUpdateInfo.version = strTrim3;
                                                        }
                                                    } else if (strTrim2.equals("can_not_skip")) {
                                                        pythonSdkUpdateInfo.can_not_skip = Boolean.parseBoolean(strTrim3);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            z = false;
                            pythonSdkUpdateInfo.document = message.media.getDocument();
                            if (!pythonSdkUpdateInfo.canInstall()) {
                                pythonSdkUpdateInfo.clear();
                            } else {
                                pythonSdkUpdateInfo.text = sb.toString();
                                ArrayList<TLRPC.MessageEntity> arrayList2 = new ArrayList<>();
                                ArrayList arrayList3 = message.entities;
                                int size2 = arrayList3.size();
                                int i2 = 0;
                                while (i2 < size2) {
                                    Object obj2 = arrayList3.get(i2);
                                    i2++;
                                    TLRPC.MessageEntity messageEntity = (TLRPC.MessageEntity) obj2;
                                    if (!(messageEntity instanceof TLRPC.TL_messageEntityPre)) {
                                        arrayList2.add(messageEntity);
                                    }
                                }
                                pythonSdkUpdateInfo.entities = arrayList2;
                                pythonSdkUpdateInfo.message = message;
                                break;
                            }
                        }
                    }
                }
            }
            if (pythonSdkUpdateInfo.message == null) {
                return null;
            }
            pythonSdkUpdateInfo.available = (pythonSdkUpdateInfo.document == null || TextUtils.isEmpty(pythonSdkUpdateInfo.version)) ? z : true;
            return pythonSdkUpdateInfo;
        }

        public static boolean isSdkVersionNewer(String str, boolean z) {
            return (ExteraConfig.pluginsPySdkBetaVersions || !PythonPluginsEngine.SDK_BETA) ? AppUtils.compareVersions(">", str, PythonPluginsEngine.SDK_VERSION) : !z;
        }

        public static boolean isAppVersionCompatible(String str, String str2) {
            return AppUtils.compareVersions(str, BuildVars.BUILD_VERSION_STRING, str2);
        }

        public static boolean isAppVersionCodeCompatible(String str, String str2) {
            return AppUtils.compareVersions(str, BuildVars.BUILD_VERSION, Integer.parseInt(str2));
        }

        public static void zipFolder(File file, File file2) throws IOException {
            if (file2.exists()) {
                file2.delete();
            }
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file2)));
            try {
                zipRecursive(file, file, zipOutputStream);
                zipOutputStream.close();
            } catch (Throwable th) {
                try {
                    zipOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        private static void zipRecursive(File file, File file2, ZipOutputStream zipOutputStream) throws IOException {
            File[] fileArrListFiles = file2.listFiles();
            if (fileArrListFiles == null) {
                return;
            }
            for (File file3 : fileArrListFiles) {
                String path = file.toURI().relativize(file3.toURI()).getPath();
                if (file3.isDirectory()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(path);
                    sb.append((path.endsWith("/") ? "-128110676454332" : "-128123561356220"));
                    zipOutputStream.putNextEntry(new ZipEntry(sb.toString()));
                    zipOutputStream.closeEntry();
                    zipRecursive(file, file3, zipOutputStream);
                } else {
                    zipOutputStream.putNextEntry(new ZipEntry(path));
                    FileInputStream fileInputStream = new FileInputStream(file3);
                    try {
                        byte[] bArr = new byte[8192];
                        while (true) {
                            int i = fileInputStream.read(bArr);
                            if (i <= 0) {
                                break;
                            } else {
                                zipOutputStream.write(bArr, 0, i);
                            }
                        }
                        fileInputStream.close();
                        zipOutputStream.closeEntry();
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void copyArchiveToPluginsDirectory(TLRPC.Document document, boolean z) {
            File pythonSdkUpdateFile = getPythonSdkUpdateFile();
            try {
                if (!AndroidUtilities.copyFile(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(document), pythonSdkUpdateFile)) {
                    FileLog.e("Failed to copy python sdk archive to " + pythonSdkUpdateFile.getAbsolutePath());
                    setStatus(2);
                } else if (z) {
                    PluginsController.getInstance().restart();
                } else {
                    setStatus(4);
                }
            } catch (IOException e) {
                FileLog.e("Failed to copy plugins-sdk file", e);
                setStatus(2);
            }
            isLoading = false;
        }

        public static void savePythonSdkArchive(TLRPC.Message message, TLRPC.Document document) {
            savePythonSdkArchive(message, document, false);
        }

        public static void savePythonSdkArchive(TLRPC.Message message, final TLRPC.Document document, final boolean z) {
            if (isLoading || message == null || document == null) {
                return;
            }
            MessageObject messageObject = new MessageObject(UserConfig.selectedAccount, message, false, true);
            isLoading = true;
            setStatus(3);
            if (messageObject.mediaExists) {
                copyArchiveToPluginsDirectory(document, z);
                return;
            }
            TAG = DownloadController.getInstance(UserConfig.selectedAccount).generateObserverTag();
            FileLoader.getInstance(UserConfig.selectedAccount).loadFile(document, messageObject, 1, 0);
            DownloadController.getInstance(UserConfig.selectedAccount).addLoadingFileObserver(FileLoader.getAttachFileName(document), messageObject, new DownloadController.FileDownloadProgressListener() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine.Updater.2
                @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                public void onProgressDownload(String str, long j, long j2) {
                }

                @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                public void onProgressUpload(String str, long j, long j2, boolean z2) {
                }

                @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                public void onFailedDownload(String str, boolean z2) {
                    FileLog.e("Failed to load plugins-sdk file");
                    Updater.isLoading = false;
                    Updater.setStatus(2);
                }

                @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                public void onSuccessDownload(String str) {
                    Updater.copyArchiveToPluginsDirectory(document, z);
                }

                @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                public int getObserverTag() {
                    return Updater.TAG;
                }
            });
        }
    }
}
