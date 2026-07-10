package org.rast3ck.mcrp.core.command;

import java.util.ArrayList;
import java.util.List;

public abstract class MCRPCommand {

    private final String name;

    private final List<String> aliases = new ArrayList<>();

    private String permission;

    private String description;

    private String usage;

    private boolean playerOnly;

    private final List<CommandArgument> arguments = new ArrayList<>();

    protected MCRPCommand(String name) {

        this.name = name;

    }

    public abstract CommandResult execute(CommandContext context);

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public List<CommandArgument> getArguments() {
        return arguments;
    }

}