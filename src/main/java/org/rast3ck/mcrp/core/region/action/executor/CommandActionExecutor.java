package org.rast3ck.mcrp.core.region.action.executor;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.rast3ck.mcrp.core.region.action.RegionActionExecutor;

public class CommandActionExecutor implements RegionActionExecutor {


    @Override
    public String getType() {

        return "command";

    }


    @Override
    public void execute(
            ServerPlayer player,
            String data
    ) {


        MinecraftServer server =
                player.getServer();


        if(server == null)
            return;



        CommandSourceStack source = player.createCommandSourceStack();



        server.getCommands()
                .performPrefixedCommand(
                        source,
                        data.replace(
                                "@player",
                                player.getName().getString()
                        )
                );

    }

}
