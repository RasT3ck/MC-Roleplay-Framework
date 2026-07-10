package org.rast3ck.mcrp.core.region.selection;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RegionSelectionManager {

    private static final Map<UUID, Selection> SELECTIONS = new HashMap<>();

    private RegionSelectionManager() {
    }

    public static void setFirst(ServerPlayer player, BlockPos position) {
        String dimension = player.level().dimension().location().toString();
        Selection current = SELECTIONS.get(player.getUUID());
        BlockPos second = current != null && current.dimension().equals(dimension) ? current.second() : null;
        SELECTIONS.put(player.getUUID(), new Selection(dimension, position.immutable(), second));
    }

    public static void setSecond(ServerPlayer player, BlockPos position) {
        String dimension = player.level().dimension().location().toString();
        Selection current = SELECTIONS.get(player.getUUID());
        BlockPos first = current != null && current.dimension().equals(dimension) ? current.first() : null;
        SELECTIONS.put(player.getUUID(), new Selection(dimension, first, position.immutable()));
    }

    public static Selection getComplete(UUID playerId) {
        Selection selection = SELECTIONS.get(playerId);
        return selection != null && selection.first() != null && selection.second() != null ? selection : null;
    }

    public static void clear(UUID playerId) {
        SELECTIONS.remove(playerId);
    }

    public record Selection(String dimension, BlockPos first, BlockPos second) {
    }
}
