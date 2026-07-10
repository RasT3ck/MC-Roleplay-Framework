package org.rast3ck.mcrp.core.region.action;

import net.minecraft.server.level.ServerPlayer;

public interface RegionActionExecutor {


    String getType();


    void execute(
            ServerPlayer player,
            String data
    );

}