package org.mvel2.sh;

import java.util.Map;

public interface CommandSet {
    Map<String, Command> load();
}
