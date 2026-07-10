package org.rast3ck.mcrp;

import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.storage.LevelResource;
import org.rast3ck.mcrp.core.MCRPConstants;
import org.rast3ck.mcrp.core.MCRPCore;

@Mod(MCRPConstants.MOD_ID)
public class MCRoleplayFramework {

    private static MCRPCore core;

    public MCRoleplayFramework() {
        core = new MCRPCore();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        var worldDir = event.getServer().getWorldPath(LevelResource.ROOT);
        core.initialize(worldDir);
    }

    public static MCRPCore getCore() {
        return core;
    }
}