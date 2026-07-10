package org.rast3ck.mcrp.core.economy.storage;

import org.rast3ck.mcrp.core.data.database.DatabaseManager;
import org.rast3ck.mcrp.core.economy.Transaction;

import java.sql.*;

public class TransactionStorage {

    private final DatabaseManager databaseManager;

    public TransactionStorage(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;

    }

    public void createTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id TEXT PRIMARY KEY,
                    actor_id TEXT,
                    from_account TEXT,
                    to_account TEXT,
                    amount TEXT NOT NULL,
                    type TEXT NOT NULL,
                    date TEXT NOT NULL,
                    reason TEXT
                );
                """;

        Connection connection = databaseManager.getConnection();

        try (Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {

            throw new RuntimeException("Could not create transactions table", e);

        }

    }

    public void save(Transaction transaction) {

        String sql = """
                INSERT INTO transactions
                (
                    id,
                    actor_id,
                    from_account,
                    to_account,
                    amount,
                    type,
                    date,
                    reason
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.getId().toString());

            statement.setString(2, transaction.getActorId() == null ? null : transaction.getActorId().toString());

            statement.setString(3, transaction.getFromAccountId() == null ? null : transaction.getFromAccountId().toString());

            statement.setString(4, transaction.getToAccountId() == null ? null : transaction.getToAccountId().toString());

            statement.setString(5, transaction.getAmount().toString());
            statement.setString(6, transaction.getType().name());
            statement.setString(7, transaction.getDate().toString());
            statement.setString(8, transaction.getReason());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save transaction", e);

        }

    }

    public void save(Connection connection, Transaction transaction) throws SQLException {

        String sql = """
                INSERT INTO transactions
                (id, actor_id, from_account, to_account, amount, type, date, reason)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transaction.getId().toString());
            statement.setString(2, transaction.getActorId() == null ? null : transaction.getActorId().toString());
            statement.setString(3, transaction.getFromAccountId() == null ? null : transaction.getFromAccountId().toString());
            statement.setString(4, transaction.getToAccountId() == null ? null : transaction.getToAccountId().toString());
            statement.setString(5, transaction.getAmount().toString());
            statement.setString(6, transaction.getType().name());
            statement.setString(7, transaction.getDate().toString());
            statement.setString(8, transaction.getReason());
            statement.executeUpdate();
        }
    }

}