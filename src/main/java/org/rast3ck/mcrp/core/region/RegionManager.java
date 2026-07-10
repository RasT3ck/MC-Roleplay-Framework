package org.rast3ck.mcrp.core.region;

import org.rast3ck.mcrp.core.region.storage.RegionStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class RegionManager {


    private final Map<UUID, Region> regions =
            new LinkedHashMap<>();


    private final RegionStorage storage;



    public RegionManager(
            RegionStorage storage
    ) {

        this.storage = storage;

    }



    public void register(Region region) {


        regions.put(
                region.getId(),
                region
        );


        storage.saveRegion(
                region
        );

    }



    public Region get(UUID id) {

        return regions.get(id);

    }



    public Collection<Region> getRegions() {

        return regions.values();

    }



    public void load() {


        for(Region region : storage.findAll()) {


            regions.put(
                    region.getId(),
                    region
            );

        }

    }



    public Region getRegionAt(
            String dimension,
            int x,
            int y,
            int z
    ) {


        for(Region region : regions.values()) {


            if(!region.getDimension()
                    .equals(dimension)) {

                continue;

            }


            if(region.contains(
                    x,
                    y,
                    z
            )) {

                return region;

            }

        }


        return null;

    }



    public void addPermission(
            Region region,
            UUID permissionId
    ) {


        region.addPermission(
                permissionId
        );


        storage.addPermission(
                region.getId(),
                permissionId
        );

    }

}