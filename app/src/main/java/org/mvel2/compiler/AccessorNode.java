package org.mvel2.compiler;

import java.io.Serializable;

public interface AccessorNode extends Accessor, Serializable {
    AccessorNode getNextNode();

    AccessorNode setNextNode(AccessorNode accessorNode);
}
