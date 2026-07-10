package org.rast3ck.mcrp.core.permission;

import org.rast3ck.mcrp.core.data.database.DatabaseManager;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PermissionStorage {

    private final DatabaseManager databaseManager;

    public PermissionStorage(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;

    }

    public void createTables() {

        String permissions = """
                CREATE TABLE IF NOT EXISTS permissions (
                    id TEXT PRIMARY KEY,
                    key TEXT NOT NULL UNIQUE
                );
                """;

        String rankPermissions = """
                CREATE TABLE IF NOT EXISTS job_rank_permissions (
                    rank_id TEXT NOT NULL,
                    permission_id TEXT NOT NULL,
                    PRIMARY KEY(rank_id, permission_id)
                );
                """;

        Connection connection = databaseManager.getConnection();

        try (Statement statement = connection.createStatement()) {

            statement.execute(permissions);
            statement.execute(rankPermissions);

        } catch (SQLException e) {

            throw new RuntimeException("Could not create permission tables", e);

        }

    }

    public void savePermission(Permission permission) {

        String sql = """
                INSERT OR REPLACE INTO permissions
                (
                    id,
                    key
                )
                VALUES (?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, permission.getId().toString());
            statement.setString(2, permission.getKey());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save permission", e);

        }

    }

    public void addRankPermission(UUID rankId, UUID permissionId) {

        String sql = """
                INSERT OR REPLACE INTO job_rank_permissions
                (
                    rank_id,
                    permission_id
                )
                VALUES (?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, rankId.toString());
            statement.setString(2, permissionId.toString());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save rank permission", e);

        }

    }

    public Set<UUID> getRankPermissions(UUID rankId) {

        Set<UUID> result = new HashSet<>();

        String sql = "SELECT permission_id FROM job_rank_permissions WHERE rank_id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, rankId.toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                result.add(UUID.fromString(rs.getString("permission_id")));

            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load rank permissions", e);

        }

        return result;

    }

    public Permission findPermission(UUID permissionId) {

        String sql = "SELECT id, key FROM permissions WHERE id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, permissionId.toString());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Permission(UUID.fromString(result.getString("id")), result.getString("key"));
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load permission", e);

        }

        return null;
    }

    public Permission findPermission(String key) {

        String sql = "SELECT id, key FROM permissions WHERE key = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, key);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Permission(UUID.fromString(result.getString("id")), result.getString("key"));
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load permission", e);

        }

        return null;
    }

    public void removeRankPermission(UUID rankId, UUID permissionId) {

        String sql = """
                DELETE FROM job_rank_permissions
                WHERE rank_id = ?
                AND permission_id = ?
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, rankId.toString());
            statement.setString(2, permissionId.toString());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not remove rank permission", e);

        }

    }
}