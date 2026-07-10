package org.rast3ck.mcrp.core.region.action;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ActionManager {

    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<String, RegionActionExecutor> executors = new HashMap<>();

    public void register(RegionActionExecutor executor) {

        if (executor == null) return;

        String type = executor.getType();

        if (type == null || type.isEmpty()) return;

        executors.put(type.toLowerCase(), executor);

    }

    public void execute(ServerPlayer player, RegionAction action) {

        if (player == null || action == null) return;

        String raw = action.getAction();

        if (raw == null || raw.isEmpty()) return;

        String[] split = raw.split(":", 2);

        if (split.length < 2) return;

        String type = split[0].toLowerCase();

        String data = split[1];

        RegionActionExecutor executor = executors.get(type);

        if (executor == null) return;

        try {
            executor.execute(player, data);

        } catch (Exception e) {
            LOGGER.error("Could not execute region action '{}' for player {}", raw, player.getGameProfile().getName(), e);
        }
    }

    public boolean hasExecutor(String type) {
        return executors.containsKey(
                type.toLowerCase()
        );
    }
}
