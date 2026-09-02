package org.mvel2.jsr223;

import java.io.Serializable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

public class MvelCompiledScript extends CompiledScript {
    private final Serializable compiledScript;
    private final MvelScriptEngine scriptEngine;

    public MvelCompiledScript(MvelScriptEngine mvelScriptEngine, Serializable serializable) {
        this.scriptEngine = mvelScriptEngine;
        this.compiledScript = serializable;
    }

    public Object eval(ScriptContext scriptContext) {
        return this.scriptEngine.evaluate(this.compiledScript, scriptContext);
    }

    public ScriptEngine getEngine() {
        return this.scriptEngine;
    }
}
