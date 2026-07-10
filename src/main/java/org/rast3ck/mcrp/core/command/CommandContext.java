package org.rast3ck.mcrp.core.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandContext {

    private final CommandSourceStack source;

    private final ServerPlayer player;

    private final String label;

    private final String[] args;

    public CommandContext(
            CommandSourceStack source,
            ServerPlayer player,
            String label,
            String[] args
    ) {

        this.source = source;
        this.player = player;
        this.label = label;
        this.args = args;

    }

    public CommandSourceStack getSource() {
        return source;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean isPlayer() {
        return player != null;
    }

}