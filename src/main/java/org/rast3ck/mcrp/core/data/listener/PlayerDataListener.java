package org.rast3ck.mcrp.core.data.listener;

import org.rast3ck.mcrp.core.data.DataManager;
import org.rast3ck.mcrp.core.economy.AccountManager;
import org.rast3ck.mcrp.core.event.EventBus;
import org.rast3ck.mcrp.core.event.player.MCRPPlayerLogoutEvent;
import org.rast3ck.mcrp.core.job.JobManager;
import org.rast3ck.mcrp.core.region.selection.RegionSelectionManager;

public final class PlayerDataListener {

    public PlayerDataListener(EventBus eventBus, DataManager dataManager, JobManager jobManager, AccountManager accountManager) {
        eventBus.register(MCRPPlayerLogoutEvent.class, event -> {

            var playerId = event.getPlayer().getUUID();

            jobManager.setOffDuty(playerId);
            jobManager.unload(playerId);

            accountManager.unload(playerId);

            RegionSelectionManager.clear(playerId);

            dataManager.unload(playerId);
        });
    }
}