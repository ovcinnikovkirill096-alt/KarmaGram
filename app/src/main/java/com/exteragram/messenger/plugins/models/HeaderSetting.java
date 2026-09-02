package com.exteragram.messenger.plugins.models;

public class HeaderSetting extends SettingItem {
    public String text;

    public HeaderSetting(String str) {
        super("header");
        this.text = str;
    }
}
