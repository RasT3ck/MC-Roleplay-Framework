package org.rast3ck.mcrp.core.command;

import org.rast3ck.mcrp.core.permission.PermissionManager;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandManager {

    private final Map<String, MCRPCommand> commands = new LinkedHashMap<>();

    private final PermissionManager permissionManager;

    public CommandManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void register(MCRPCommand command) {

        commands.put(command.getName().toLowerCase(), command);

        for (String alias : command.getAliases()) {

            commands.put(alias.toLowerCase(), command);

        }

    }

    public void unregister(String name) {

        commands.remove(name.toLowerCase());

    }

    public boolean hasCommand(String name) {

        return commands.containsKey(name.toLowerCase());

    }

    public MCRPCommand getCommand(String name) {

        return commands.get(name.toLowerCase());

    }

    public Collection<MCRPCommand> getCommands() {

        return commands.values();

    }

    public CommandResult execute(CommandContext context) {

        MCRPCommand command = getCommand(context.getLabel());

        if (command == null) {

            return CommandResult.NOT_FOUND;

        }

        if (command.isPlayerOnly() && !context.isPlayer()) {

            return CommandResult.PLAYER_ONLY;

        }

        if (command.getPermission() != null && !command.getPermission().isEmpty() && context.isPlayer() && !permissionManager.hasPermission(context.getPlayer().getUUID(), command.getPermission())) {

            return CommandResult.NO_PERMISSION;

        }

        CommandResult result = command.execute(context);

        switch (result) {

            case SUCCESS -> {
                // Nada
            }

            case NO_PERMISSION -> {
                if (context.isPlayer()) {
                    context.getPlayer().sendSystemMessage(net.minecraft.network.chat.Component.literal("No tienes permiso para usar este comando."));
                }
            }

            case INVALID_USAGE -> {
                if (context.isPlayer() && command.getUsage() != null) {
                    context.getPlayer().sendSystemMessage(net.minecraft.network.chat.Component.literal("Uso: " + command.getUsage()));
                }
            }

            case PLAYER_ONLY -> {
                if (context.isPlayer()) {
                    context.getPlayer().sendSystemMessage(net.minecraft.network.chat.Component.literal("Este comando solo puede ejecutarlo un jugador."));
                }
            }

            case CONSOLE_ONLY -> {
                if (context.isPlayer()) {
                    context.getPlayer().sendSystemMessage(net.minecraft.network.chat.Component.literal("Este comando solo puede ejecutarlo la consola."));
                }
            }

            case DISABLED -> {
                if (context.isPlayer()) {
                    context.getPlayer().sendSystemMessage(net.minecraft.network.chat.Component.literal("Este comando está deshabilitado."));
                }
            }

            case ERROR -> {
                org.rast3ck.mcrp.core.logger.MCRPLogger.error("Error ejecutando el comando '" + context.getLabel() + "'");
            }

            default -> {
            }

        }

        return result;

    }

}