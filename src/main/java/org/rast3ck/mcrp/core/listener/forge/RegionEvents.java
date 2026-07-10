package org.rast3ck.mcrp.core.listener.forge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.MCRPConstants;
import org.rast3ck.mcrp.core.event.region.MCRPRegionEnterEvent;
import org.rast3ck.mcrp.core.event.region.MCRPRegionLeaveEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.rast3ck.mcrp.core.event.region.MCRPRegionInteractEvent;
import org.rast3ck.mcrp.core.region.Region;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MCRPConstants.MOD_ID)
public final class RegionEvents {

    private static final Map<UUID, Region> currentRegions = new HashMap<>();

    private static final Map<UUID, Position> lastPositions = new HashMap<>();

    private RegionEvents() {

    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.player.level().isClientSide) return;
        if (!(event.player instanceof ServerPlayer player)) return;

        UUID uuid = player.getUUID();

        String dimension = player.level().dimension().location().toString();

        Position current = new Position(dimension, player.getBlockX(), player.getBlockY(), player.getBlockZ());

        Position previous = lastPositions.get(uuid);

        // El jugador sigue en el mismo bloque y dimensión
        if (current.equals(previous)) return;

        lastPositions.put(uuid, current);

        Region region = MCRoleplayFramework.getCore().getRegionManager().getRegionAt(dimension, current.x(), current.y(), current.z());

        Region oldRegion = currentRegions.get(uuid);

        if (region != null && oldRegion != null && region.getId().equals(oldRegion.getId())) return;

        if (oldRegion != null) {

            MCRoleplayFramework.getCore().getEventBus().post(new MCRPRegionLeaveEvent(player, oldRegion));
        }

        if (region != null) {

            MCRoleplayFramework.getCore().getEventBus().post(new MCRPRegionEnterEvent(player, region));

            currentRegions.put(uuid, region);

        } else {
            currentRegions.remove(uuid);
        }

    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {


        UUID uuid = event.getEntity().getUUID();


        currentRegions.remove(uuid);

        lastPositions.remove(uuid);

    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {


        if (event.getEntity().level().isClientSide) return;


        if (!(event.getEntity() instanceof ServerPlayer player)) return;


        Region region = MCRoleplayFramework.getCore().getRegionManager().getRegionAt(

                player.level().dimension().location().toString(),

                event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()

        );


        if (region == null) return;


        MCRoleplayFramework.getCore().getEventBus().post(new MCRPRegionInteractEvent(player, region));

    }

    private record Position(String dimension, int x, int y, int z) {
    }

}