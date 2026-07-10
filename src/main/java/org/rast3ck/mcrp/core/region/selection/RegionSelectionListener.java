package org.rast3ck.mcrp.core.region.selection;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.rast3ck.mcrp.core.MCRPConstants;

@Mod.EventBusSubscriber(modid = MCRPConstants.MOD_ID)
public final class RegionSelectionListener {

    private RegionSelectionListener() {
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.level().isClientSide
                || !player.getMainHandItem().is(Items.WOODEN_AXE)) {
            return;
        }

        RegionSelectionManager.setFirst(player, event.getPos());
        player.sendSystemMessage(Component.literal("Primera esquina: " + event.getPos().toShortString()));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.level().isClientSide
                || !player.getMainHandItem().is(Items.WOODEN_AXE)) {
            return;
        }

        RegionSelectionManager.setSecond(player, event.getPos());
        player.sendSystemMessage(Component.literal("Segunda esquina: " + event.getPos().toShortString()));
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
