package org.mvel2.util;

import java.io.Serializable;
import org.mvel2.integration.VariableResolverFactory;

public interface StaticStub extends Serializable {
    Object call(Object obj, Object obj2, VariableResolverFactory variableResolverFactory, Object[] objArr);
}
