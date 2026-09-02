package com.exteragram.messenger.plugins;

import java.util.ArrayList;
import java.util.List;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

public class Plugin {
    public transient PluginsController.PluginsEngine cachedEngine;
    private String engine;
    private final String id;
    private final String name;
    private String pack = null;
    private int index = -1;
    private Throwable error = null;
    private boolean isNotResponding = false;
    private List<String> requirements = new ArrayList();
    private String version = "1.0";
    private String appVersion = ">=12.5.1";
    private String sdkVersion = ">=" + PythonPluginsEngine.SDK_VERSION;
    private String description = LocaleController.getString(R.string.PluginNoDescription);
    private String author = LocaleController.getString(R.string.PluginNoAuthor);
    private boolean isEnabled = false;

    public Plugin(String str, String str2) {
        this.id = str;
        this.name = str2;
    }

    private static boolean isIconValid(String str) {
        return str != null && str.matches("^[a-zA-Z][a-zA-Z0-9_]*/\\d+$");
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getEngine() {
        return this.engine;
    }

    public void setEngine(String str) {
        this.engine = str;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String str) {
        this.author = str;
    }

    public String getName() {
        return this.name;
    }

    public boolean isEnabled() {
        return !hasError() && this.isEnabled;
    }

    public void setEnabled(boolean z) {
        this.isEnabled = z && !hasError();
    }

    public Throwable getError() {
        return this.error;
    }

    public void setError(Throwable th) {
        this.error = th;
        if (hasError()) {
            this.isEnabled = false;
        }
    }

    public boolean hasError() {
        return this.error != null;
    }

    public String getPack() {
        return this.pack;
    }

    public int getIndex() {
        return this.index;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public List<String> getRequirements() {
        return this.requirements;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String str) {
        this.appVersion = str;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(String str) {
        this.sdkVersion = str;
    }

    public void setRequirements(List<String> list) {
        this.requirements = list;
    }

    public boolean isNotResponding() {
        return this.isNotResponding;
    }

    public void setNotResponding(boolean z) {
        this.isNotResponding = z;
    }

    public String getIcon() {
        if (this.pack == null || this.index < 0) {
            return null;
        }
        return this.pack + "/" + this.index;
    }

    public void setIcon(String str) {
        int iLastIndexOf;
        if (isIconValid(str) && (iLastIndexOf = str.lastIndexOf(47)) != -1) {
            this.pack = str.substring(0, iLastIndexOf);
            this.index = Integer.parseInt(str.substring(iLastIndexOf + 1));
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof Plugin) {
            return ((Plugin) obj).id.equals(this.id);
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return this.id.hashCode();
    }
}
