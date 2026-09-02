package androidx.camera.core.impl;

public interface MutableConfig extends Config {
    void insertOption(Config.Option option, Config.OptionPriority optionPriority, Object obj);

    void insertOption(Config.Option option, Object obj);
}
