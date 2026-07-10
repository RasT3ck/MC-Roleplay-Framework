package org.rast3ck.mcrp.core.listener.forge;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.MCRPConstants;
import org.rast3ck.mcrp.core.event.player.MCRPPlayerJoinEvent;
import org.rast3ck.mcrp.core.event.player.MCRPPlayerLogoutEvent;


@Mod.EventBusSubscriber(modid = MCRPConstants.MOD_ID)
public final class PlayerEvents {

    private PlayerEvents() {

    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        MCRoleplayFramework.getCore()
                .getEventBus()
                .post(
                        new MCRPPlayerJoinEvent(
                                event.getEntity()
                        )
                );

    }


    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {

        MCRoleplayFramework.getCore()
                .getEventBus()
                .post(
                        new MCRPPlayerLogoutEvent(
                                event.getEntity()
                        )
                );

    }

}