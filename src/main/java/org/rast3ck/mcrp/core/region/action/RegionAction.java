package org.rast3ck.mcrp.core.region.action;

import java.util.Objects;
import java.util.UUID;

public class RegionAction {

    private final UUID id;

    private final UUID regionId;

    private final RegionActionType type;

    private final String action;

    public RegionAction(UUID id, UUID regionId, RegionActionType type, String action) {
        this.id = id;
        this.regionId = regionId;
        this.type = type;
        this.action = action;
    }

    public UUID getId() {
        return id;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public RegionActionType getType() {
        return type;
    }

    public String getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;


        if (!(o instanceof RegionAction other))
            return false;


        return Objects.equals(
                id,
                other.id
        );

    }

    @Override
    public int hashCode() {

        return Objects.hash(id);

    }

}