package org.mvel2.sh;

public interface Command {
    Object execute(ShellSession shellSession, String[] strArr);

    String getDescription();

    String getHelp();
}
