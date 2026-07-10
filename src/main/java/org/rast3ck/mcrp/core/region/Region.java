package org.rast3ck.mcrp.core.region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Region {

    private final List<UUID> permissions = new ArrayList<>();

    private final UUID id;

    private final String name;

    private final RegionType type;

    private final String dimension;

    private final int minX;
    private final int minY;
    private final int minZ;

    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public Region(UUID id, String name, RegionType type, String dimension, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.dimension = dimension;

        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;

        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RegionType getType() {
        return type;
    }

    public String getDimension() {
        return dimension;
    }

    public List<UUID> getPermissions() {
        return permissions;
    }

    public void addPermission(UUID permissionId) {
        permissions.add(permissionId);
    }

    public boolean contains(int x, int y, int z) {

        return x >= minX &&
                x <= maxX &&

                y >= minY &&
                y <= maxY &&

                z >= minZ &&
                z <= maxZ;

    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

}