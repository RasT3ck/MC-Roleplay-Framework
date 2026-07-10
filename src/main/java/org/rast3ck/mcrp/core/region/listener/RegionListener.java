package org.rast3ck.mcrp.core.region.listener;

import org.rast3ck.mcrp.core.event.EventBus;
import org.rast3ck.mcrp.core.event.region.MCRPRegionEnterEvent;
import org.rast3ck.mcrp.core.event.region.MCRPRegionInteractEvent;
import org.rast3ck.mcrp.core.event.region.MCRPRegionLeaveEvent;
import org.rast3ck.mcrp.core.region.action.ActionManager;
import org.rast3ck.mcrp.core.region.action.RegionActionManager;
import org.rast3ck.mcrp.core.region.action.RegionActionType;
import org.rast3ck.mcrp.core.region.Region;
import org.rast3ck.mcrp.core.permission.PermissionManager;

public class RegionListener {

    private final RegionActionManager regionActionManager;
    private final ActionManager actionManager;
    private final PermissionManager permissionManager;

    public RegionListener(
            EventBus eventBus,
            RegionActionManager regionActionManager,
            ActionManager actionManager,
            PermissionManager permissionManager
    ) {

        this.regionActionManager = regionActionManager;
        this.actionManager = actionManager;
        this.permissionManager = permissionManager;


        eventBus.register(MCRPRegionEnterEvent.class, this::onEnter);


        eventBus.register(MCRPRegionLeaveEvent.class, this::onLeave);

        eventBus.register(MCRPRegionInteractEvent.class, this::onInteract);
    }

    private void onEnter(MCRPRegionEnterEvent event) {

        if (!hasAccess(event.getPlayer().getUUID(), event.getRegion())) return;

        regionActionManager.getActions(event.getRegion().getId(), RegionActionType.ENTER).forEach(action -> actionManager.execute(event.getPlayer(), action));

    }

    private void onLeave(MCRPRegionLeaveEvent event) {

        if (!hasAccess(event.getPlayer().getUUID(), event.getRegion())) return;

        regionActionManager.getActions(event.getRegion().getId(), RegionActionType.LEAVE).forEach(action -> actionManager.execute(event.getPlayer(), action));

    }

    private void onInteract(MCRPRegionInteractEvent event) {

        if (!hasAccess(event.getPlayer().getUUID(), event.getRegion())) return;

        regionActionManager
                .getActions(
                        event.getRegion().getId(),
                        RegionActionType.INTERACT
                )
                .forEach(
                        action ->
                                actionManager.execute(
                                        event.getPlayer(),
                                        action
                                )
                );

    }

    private boolean hasAccess(java.util.UUID playerId, Region region) {
        return region.getPermissions().isEmpty()
                || region.getPermissions().stream()
                .anyMatch(permissionId -> permissionManager.hasPermission(playerId, permissionId));
    }

}
