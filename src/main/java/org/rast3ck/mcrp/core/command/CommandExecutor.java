package org.rast3ck.mcrp.core.command;

@FunctionalInterface
public interface CommandExecutor {

    void execute(CommandContext context);

}