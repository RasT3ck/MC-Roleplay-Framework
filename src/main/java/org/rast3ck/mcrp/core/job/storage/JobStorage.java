package org.rast3ck.mcrp.core.job.storage;

import org.rast3ck.mcrp.core.data.database.DatabaseManager;
import org.rast3ck.mcrp.core.job.*;

import java.sql.*;
import java.util.UUID;

public class JobStorage {

    private final DatabaseManager databaseManager;

    public JobStorage(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createTables() {

        String jobs = """
                CREATE TABLE IF NOT EXISTS jobs (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    salary TEXT NOT NULL
                );
                """;

        String ranks = """
                CREATE TABLE IF NOT EXISTS job_ranks (
                    id TEXT PRIMARY KEY,
                    job_id TEXT NOT NULL,
                    name TEXT NOT NULL,
                    level INTEGER NOT NULL
                );
                """;

        String playerJobs = """
                CREATE TABLE IF NOT EXISTS player_jobs (
                    player_id TEXT PRIMARY KEY,
                    job_id TEXT NOT NULL,
                    rank_id TEXT NOT NULL,
                    on_duty INTEGER NOT NULL DEFAULT 0,
                    worked_seconds INTEGER NOT NULL DEFAULT 0
                );
                """;

        Connection connection = databaseManager.getConnection();

        try (Statement statement = connection.createStatement()) {

            statement.execute(jobs);
            statement.execute(ranks);
            statement.execute(playerJobs);
            ensurePlayerJobColumns(connection);

        } catch (SQLException e) {

            throw new RuntimeException("Could not create job tables", e);

        }

    }

    private void ensurePlayerJobColumns(Connection connection) throws SQLException {

        boolean hasOnDuty = false;
        boolean hasWorkedSeconds = false;

        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery("PRAGMA table_info(player_jobs)")) {
            while (result.next()) {
                String column = result.getString("name");
                hasOnDuty |= "on_duty".equals(column);
                hasWorkedSeconds |= "worked_seconds".equals(column);
            }
        }

        try (Statement statement = connection.createStatement()) {
            if (!hasOnDuty) statement.execute("ALTER TABLE player_jobs ADD COLUMN on_duty INTEGER NOT NULL DEFAULT 0");
            if (!hasWorkedSeconds)
                statement.execute("ALTER TABLE player_jobs ADD COLUMN worked_seconds INTEGER NOT NULL DEFAULT 0");
        }
    }

    public void savePlayerJob(PlayerJob playerJob) {

        String sql = """
                INSERT OR REPLACE INTO player_jobs
                (
                    player_id,
                    job_id,
                    rank_id,
                    on_duty,
                    worked_seconds
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerJob.getPlayerId().toString());
            statement.setString(2, playerJob.getJobId().toString());
            statement.setString(3, playerJob.getRankId().toString());
            statement.setInt(4, playerJob.isOnDuty() ? 1 : 0);
            statement.setLong(5, playerJob.getWorkedSeconds());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save player job", e);

        }

    }

    public void deletePlayerJob(UUID playerId) {

        String sql = "DELETE FROM player_jobs WHERE player_id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerId.toString());
            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not delete player job", e);

        }

    }

    public PlayerJob findPlayerJob(UUID playerId) {

        String sql = "SELECT * FROM player_jobs WHERE player_id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerId.toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                PlayerJob playerJob = new PlayerJob(

                        UUID.fromString(result.getString("player_id")), UUID.fromString(result.getString("job_id")), UUID.fromString(result.getString("rank_id"))

                );

                playerJob.setOnDuty(result.getBoolean("on_duty"));
                playerJob.setWorkedSeconds(result.getLong("worked_seconds"));

                return playerJob;

            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load player job", e);

        }

        return null;

    }

    public void saveJob(Job job) {

        String sql = """
                INSERT OR REPLACE INTO jobs
                (
                    id,
                    name,
                    salary
                )
                VALUES (?, ?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, job.getId().toString());
            statement.setString(2, job.getName());
            statement.setString(3, job.getSalary().toString());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save job", e);

        }

    }

    public void saveRank(JobRank rank) {

        String sql = """
                INSERT OR REPLACE INTO job_ranks
                (
                    id,
                    job_id,
                    name,
                    level
                )
                VALUES (?, ?, ?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, rank.getId().toString());
            statement.setString(2, rank.getJobId().toString());
            statement.setString(3, rank.getName());
            statement.setInt(4, rank.getLevel());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save rank", e);

        }

    }

    public Job findJob(UUID id) {

        String sql = "SELECT * FROM jobs WHERE id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id.toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                return new Job(

                        UUID.fromString(result.getString("id")), result.getString("name"), new java.math.BigDecimal(result.getString("salary"))

                );

            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load job", e);

        }

        return null;

    }

    public JobRank findRank(UUID id) {

        String sql = "SELECT * FROM job_ranks WHERE id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id.toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                return new JobRank(

                        UUID.fromString(result.getString("id")), UUID.fromString(result.getString("job_id")), result.getString("name"), result.getInt("level")

                );

            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load rank", e);

        }

        return null;

    }
}