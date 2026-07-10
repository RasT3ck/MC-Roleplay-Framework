package org.rast3ck.mcrp.core.task.listener;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.MCRPConstants;

@Mod.EventBusSubscriber(modid = MCRPConstants.MOD_ID)
public class ServerTickListener {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        MCRoleplayFramework.getCore()
                .getTaskManager()
                .tick();

    }
}
