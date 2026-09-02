package org.mvel2.templates.util;

public interface TemplateOutputStream {
    TemplateOutputStream append(CharSequence charSequence);

    TemplateOutputStream append(char[] cArr);
}
