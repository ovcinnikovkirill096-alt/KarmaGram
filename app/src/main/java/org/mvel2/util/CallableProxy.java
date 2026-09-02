package org.mvel2.util;

import org.mvel2.integration.VariableResolverFactory;

public interface CallableProxy {
    Object call(Object obj, Object obj2, VariableResolverFactory variableResolverFactory, Object[] objArr);
}
