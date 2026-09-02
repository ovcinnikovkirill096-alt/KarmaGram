package org.mvel2.optimizers;

import org.mvel2.compiler.Accessor;

public interface OptimizerHook {
    Accessor generateAccessor(AccessorOptimizer accessorOptimizer);

    boolean isOptimizerSupported(Class<? extends AccessorOptimizer> cls);
}
