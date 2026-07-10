package org.rast3ck.mcrp.core.registry;

import org.rast3ck.mcrp.core.region.action.RegionAction;

import java.util.UUID;

public class RegionActionRegistry
        extends GenericRegistry<UUID, RegionAction> {

    public RegionActionRegistry() {

        super(RegionAction::getId);

    }

}
