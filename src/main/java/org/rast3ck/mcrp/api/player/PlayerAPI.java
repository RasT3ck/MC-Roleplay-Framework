package org.rast3ck.mcrp.api.player;

import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.data.PlayerData;

import java.util.UUID;

public final class PlayerAPI {

    public static final PlayerAPI INSTANCE =
            new PlayerAPI();


    private PlayerAPI() {

    }


    public PlayerData getData(UUID playerId) {

        return MCRoleplayFramework.getCore()
                .getDataManager()
                .getPlayerData(playerId);

    }

}