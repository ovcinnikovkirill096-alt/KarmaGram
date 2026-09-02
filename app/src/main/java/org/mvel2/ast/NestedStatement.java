package org.mvel2.ast;

import org.mvel2.compiler.ExecutableStatement;

public interface NestedStatement {
    ExecutableStatement getNestedStatement();
}
