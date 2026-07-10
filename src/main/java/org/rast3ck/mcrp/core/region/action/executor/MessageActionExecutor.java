package org.rast3ck.mcrp.core.region.action.executor;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.rast3ck.mcrp.core.region.action.RegionActionExecutor;

public class MessageActionExecutor
        implements RegionActionExecutor {


    @Override
    public String getType() {

        return "message";

    }



    @Override
    public void execute(
            ServerPlayer player,
            String data
    ){

        player.sendSystemMessage(
                Component.literal(data)
        );

    }

}