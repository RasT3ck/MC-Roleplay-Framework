package org.rast3ck.mcrp.core.registry;

import org.rast3ck.mcrp.core.region.Region;

import java.util.UUID;

public class RegionRegistry
        extends GenericRegistry<UUID, Region> {

    public RegionRegistry() {

        super(Region::getId);

    }

}
