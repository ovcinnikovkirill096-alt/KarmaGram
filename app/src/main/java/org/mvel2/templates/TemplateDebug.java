package org.mvel2.templates;

import org.mvel2.templates.res.Node;

public class TemplateDebug {
    public static void decompile(CompiledTemplate compiledTemplate, char[] cArr) {
        Node root = compiledTemplate.getRoot();
        int i = 1;
        while (root != null) {
            System.out.println(i + "> " + root.toString() + "['" + new String(cArr, root.getBegin(), root.getEnd() - root.getBegin()) + "']");
            root = root.getNext();
            i++;
        }
    }
}
