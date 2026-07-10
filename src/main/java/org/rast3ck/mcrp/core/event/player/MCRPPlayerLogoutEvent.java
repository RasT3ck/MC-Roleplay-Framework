package org.rast3ck.mcrp.core.event.player;

import net.minecraft.world.entity.player.Player;
import org.rast3ck.mcrp.core.event.MCRPEvent;

public class MCRPPlayerLogoutEvent extends MCRPEvent {

    private final Player player;

    public MCRPPlayerLogoutEvent(Player player) {

        this.player = player;

    }

    public Player getPlayer() {

        return player;

    }

}