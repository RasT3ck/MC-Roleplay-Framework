package org.rast3ck.mcrp.core.data;

import java.util.UUID;

public interface IDataStorage {

    PlayerData load(UUID playerId);

    void save(UUID playerId, PlayerData data);

    void delete(UUID playerId);

}