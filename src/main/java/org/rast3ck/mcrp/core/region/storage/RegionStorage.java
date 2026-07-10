package org.rast3ck.mcrp.core.region.storage;

import org.rast3ck.mcrp.core.data.database.DatabaseManager;
import org.rast3ck.mcrp.core.region.Region;
import org.rast3ck.mcrp.core.region.RegionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegionStorage {

    private final DatabaseManager databaseManager;

    public RegionStorage(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createTables() {

        String regions = """
                CREATE TABLE IF NOT EXISTS regions (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    type TEXT NOT NULL,
                    dimension TEXT NOT NULL,
                    min_x INTEGER NOT NULL,
                    min_y INTEGER NOT NULL,
                    min_z INTEGER NOT NULL,
                    max_x INTEGER NOT NULL,
                    max_y INTEGER NOT NULL,
                    max_z INTEGER NOT NULL
                );
                """;

        String permissions = """
                CREATE TABLE IF NOT EXISTS region_permissions (
                    region_id TEXT NOT NULL,
                    permission_id TEXT NOT NULL,
                    PRIMARY KEY(region_id, permission_id)
                );
                """;

        Connection connection = databaseManager.getConnection();

        try (Statement statement = connection.createStatement()) {

            statement.execute(regions);
            statement.execute(permissions);

        } catch (SQLException e) {

            throw new RuntimeException("Could not create region tables", e);

        }

    }

    public void saveRegion(Region region) {

        String sql = """
                INSERT OR REPLACE INTO regions
                (
                    id,
                    name,
                    type,
                    dimension,
                    min_x,
                    min_y,
                    min_z,
                    max_x,
                    max_y,
                    max_z
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, region.getId().toString());
            statement.setString(2, region.getName());
            statement.setString(3, region.getType().name());
            statement.setString(4, region.getDimension());
            statement.setInt(5, region.getMinX());
            statement.setInt(6, region.getMinY());
            statement.setInt(7, region.getMinZ());
            statement.setInt(8, region.getMaxX());
            statement.setInt(9, region.getMaxY());
            statement.setInt(10, region.getMaxZ());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save region", e);

        }

    }

    public void addPermission(UUID regionId, UUID permissionId) {

        String sql = """
                INSERT OR REPLACE INTO region_permissions
                (
                    region_id,
                    permission_id
                )
                VALUES (?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, regionId.toString());
            statement.setString(2, permissionId.toString());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save region permission", e);

        }

    }

    public Region find(UUID id) {

        String sql = "SELECT * FROM regions WHERE id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id.toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Region region = new Region(

                        UUID.fromString(result.getString("id")), result.getString("name"), RegionType.valueOf(result.getString("type")), result.getString("dimension"),

                        result.getInt("min_x"), result.getInt("min_y"), result.getInt("min_z"),

                        result.getInt("max_x"), result.getInt("max_y"), result.getInt("max_z")

                );

                loadPermissions(connection, region);

                return region;

            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load region", e);

        }

        return null;

    }

    public List<Region> findAll() {

        List<Region> regions = new ArrayList<>();

        String sql = "SELECT id FROM regions";

        Connection connection = databaseManager.getConnection();

        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                Region region = find(UUID.fromString(result.getString("id")));

                if (region != null) {

                    regions.add(region);

                }

            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load regions", e);

        }

        return regions;

    }

    private void loadPermissions(Connection connection, Region region) throws SQLException {

        String sql = "SELECT permission_id FROM region_permissions WHERE region_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, region.getId().toString());

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                region.addPermission(UUID.fromString(result.getString("permission_id")));

            }

        }

    }

}