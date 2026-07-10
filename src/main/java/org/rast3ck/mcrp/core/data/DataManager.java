package org.rast3ck.mcrp.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private final IDataStorage storage;


    private final Map<UUID, PlayerData> cache =
            new HashMap<>();


    public DataManager(IDataStorage storage) {

        this.storage = storage;
    }


    public PlayerData getPlayerData(UUID uuid) {

        return cache.computeIfAbsent(
                uuid,
                id -> storage.load(id)
        );
    }


    public void save(UUID uuid) {

        PlayerData data = cache.get(uuid);

        if (data != null) {

            storage.save(uuid, data);
        }
    }


    public void unload(UUID uuid) {

        save(uuid);

        cache.remove(uuid);
    }
}