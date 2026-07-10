package org.rast3ck.mcrp.core.region;

import java.util.UUID;

public class RegionPermission {

    private final UUID regionId;

    private final UUID permissionId;


    public RegionPermission(
            UUID regionId,
            UUID permissionId
    ) {

        this.regionId = regionId;
        this.permissionId = permissionId;

    }


    public UUID getRegionId() {

        return regionId;

    }


    public UUID getPermissionId() {

        return permissionId;

    }

}