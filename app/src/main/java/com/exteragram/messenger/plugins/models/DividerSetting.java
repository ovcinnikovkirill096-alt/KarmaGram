package com.exteragram.messenger.plugins.models;

public class DividerSetting extends SettingItem {
    public String text;

    public DividerSetting(String str) {
        super("divider");
        this.text = str;
    }
}
