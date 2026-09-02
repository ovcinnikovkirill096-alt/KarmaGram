package org.mvel2.templates;

import java.util.Iterator;
import java.util.Set;

public interface TemplateRegistry {
    void addNamedTemplate(String str, CompiledTemplate compiledTemplate);

    boolean contains(String str);

    CompiledTemplate getNamedTemplate(String str);

    Set<String> getNames();

    Iterator iterator();
}
