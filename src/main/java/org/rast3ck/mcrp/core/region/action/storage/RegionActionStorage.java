package org.rast3ck.mcrp.core.region.action.storage;

import org.rast3ck.mcrp.core.data.database.DatabaseManager;
import org.rast3ck.mcrp.core.region.action.RegionAction;
import org.rast3ck.mcrp.core.region.action.RegionActionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegionActionStorage {

    private final DatabaseManager databaseManager;

    public RegionActionStorage(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS region_actions (
                
                    id TEXT PRIMARY KEY,
                
                    region_id TEXT NOT NULL,
                
                    type TEXT NOT NULL,
                
                    action TEXT NOT NULL
                
                );
                """;

        Connection connection = databaseManager.getConnection();

        try (Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {

            throw new RuntimeException("Could not create region actions table", e);

        }

    }

    public void save(RegionAction action) {

        String sql = """
                INSERT OR REPLACE INTO region_actions
                (
                    id,
                    region_id,
                    type,
                    action
                )
                VALUES (?, ?, ?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, action.getId().toString());
            statement.setString(2, action.getRegionId().toString());
            statement.setString(3, action.getType().name());
            statement.setString(4, action.getAction());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save region action", e);

        }

    }

    public List<RegionAction> findByRegion(UUID regionId) {

        List<RegionAction> actions = new ArrayList<>();

        String sql = "SELECT * FROM region_actions WHERE region_id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, regionId.toString());

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                actions.add(new RegionAction(

                        UUID.fromString(result.getString("id")), UUID.fromString(result.getString("region_id")), RegionActionType.valueOf(result.getString("type")), result.getString("action")

                ));

            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load region actions", e);

        }

        return actions;
    }

    public List<RegionAction> findAll() {

        List<RegionAction> actions = new ArrayList<>();

        String sql = "SELECT * FROM region_actions";

        Connection connection = databaseManager.getConnection();

        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                actions.add(new RegionAction(

                        UUID.fromString(result.getString("id")), UUID.fromString(result.getString("region_id")), RegionActionType.valueOf(result.getString("type")), result.getString("action")));

            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not load region actions", e);
        }

        return actions;

    }

    public void delete(UUID id) {

        String sql = """
                DELETE FROM region_actions
                WHERE id = ?
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id.toString());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not delete region action", e);

        }

    }

}