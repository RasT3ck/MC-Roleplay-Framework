package org.rast3ck.mcrp.core.permission;

import org.rast3ck.mcrp.core.job.JobManager;
import org.rast3ck.mcrp.core.job.JobRank;

import java.util.*;

public class PermissionManager {

    private final Map<String, Permission> permissions = new HashMap<>();

    private final Map<UUID, Set<String>> rankPermissions = new HashMap<>();

    private final JobManager jobManager;

    private final PermissionStorage storage;

    public PermissionManager(JobManager jobManager, PermissionStorage storage) {

        this.jobManager = jobManager;
        this.storage = storage;

    }

    public void register(Permission permission) {

        permissions.put(
                permission.getKey(),
                permission
        );

        storage.savePermission(permission);

    }

    public void addPermission(UUID rankId, String permission) {

        Permission perm = resolvePermission(permission);


        if (perm == null) {

            return;

        }


        rankPermissions
                .computeIfAbsent(rankId, this::loadRankPermissions)
                .add(permission);


        storage.addRankPermission(
                rankId,
                perm.getId()
        );

    }

    public boolean hasPermission(UUID playerId, String permission) {

        JobRank rank =
                jobManager.getPlayerRank(playerId);


        if (rank == null) {

            return false;

        }


        Set<String> permissions = rankPermissions.computeIfAbsent(
                rank.getId(),
                this::loadRankPermissions
        );


        return permissions.contains(permission);

    }

    public void removePermission(UUID rankId, String permission) {

        Permission perm = resolvePermission(permission);


        if (perm == null) {

            return;

        }


        Set<String> perms = rankPermissions.computeIfAbsent(
                rankId,
                this::loadRankPermissions
        );


        perms.remove(permission);


        storage.removeRankPermission(
                rankId,
                perm.getId()
        );

    }

    private Set<String> loadRankPermissions(UUID rankId) {

        Set<String> result = new HashSet<>();

        for (UUID permissionId : storage.getRankPermissions(rankId)) {
            Permission permission = storage.findPermission(permissionId);

            if (permission != null) {
                permissions.putIfAbsent(permission.getKey(), permission);
                result.add(permission.getKey());
            }
        }

        return result;
    }

    public boolean hasPermission(UUID playerId, UUID permissionId) {
        JobRank rank = jobManager.getPlayerRank(playerId);
        return rank != null && storage.getRankPermissions(rank.getId()).contains(permissionId);
    }

    private Permission resolvePermission(String key) {
        Permission permission = permissions.get(key);

        if (permission == null) {
            permission = storage.findPermission(key);
            if (permission != null) {
                permissions.put(permission.getKey(), permission);
            }
        }

        return permission;
    }
}
