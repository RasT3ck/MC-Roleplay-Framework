package org.rast3ck.mcrp.core.event.region;

import net.minecraft.server.level.ServerPlayer;
import org.rast3ck.mcrp.core.event.MCRPEvent;
import org.rast3ck.mcrp.core.region.Region;

public class MCRPRegionEnterEvent extends MCRPEvent {


    private final ServerPlayer player;

    private final Region region;


    public MCRPRegionEnterEvent(
            ServerPlayer player,
            Region region
    ) {

        this.player = player;
        this.region = region;

    }


    public ServerPlayer getPlayer() {

        return player;

    }


    public Region getRegion() {

        return region;

    }

}