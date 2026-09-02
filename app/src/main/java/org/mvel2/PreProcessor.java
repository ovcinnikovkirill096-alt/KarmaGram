package org.mvel2;

public interface PreProcessor {
    String parse(String str);

    char[] parse(char[] cArr);
}
