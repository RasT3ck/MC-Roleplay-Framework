package org.rast3ck.mcrp.core.economy.storage;

import org.rast3ck.mcrp.core.data.database.DatabaseManager;
import org.rast3ck.mcrp.core.economy.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;

public class AccountStorage {

    private final DatabaseManager databaseManager;
    private final CurrencyManager currencyManager;

    public AccountStorage(DatabaseManager databaseManager, CurrencyManager currencyManager) {

        this.databaseManager = databaseManager;
        this.currencyManager = currencyManager;

    }

    public void createTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS accounts (
                    id TEXT PRIMARY KEY,
                    owner_id TEXT NOT NULL,
                    name TEXT NOT NULL,
                    type TEXT NOT NULL,
                    currency TEXT NOT NULL,
                    balance TEXT NOT NULL
                );
                """;

        Connection connection = databaseManager.getConnection();

        try (Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {

            throw new RuntimeException("Could not create accounts table", e);

        }

    }

    public void save(Account account) {

        String sql = """
                INSERT OR REPLACE INTO accounts
                (
                    id,
                    owner_id,
                    name,
                    type,
                    currency,
                    balance
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getId().toString());
            statement.setString(2, account.getOwnerId().toString());
            statement.setString(3, account.getName());
            statement.setString(4, account.getType().name());
            statement.setString(5, account.getCurrency().getId());
            statement.setString(6, account.getBalance().toString());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not save account", e);

        }

    }

    public Account findByOwner(UUID ownerId) {

        String sql = "SELECT * FROM accounts WHERE owner_id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, ownerId.toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                return loadAccount(result);

            }

        } catch (SQLException e) {

            throw new RuntimeException("Could not load account", e);

        }

        return null;

    }

    public void save(Connection connection, Account account) throws SQLException {

        String sql = """
                INSERT OR REPLACE INTO accounts
                (id, owner_id, name, type, currency, balance)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getId().toString());
            statement.setString(2, account.getOwnerId().toString());
            statement.setString(3, account.getName());
            statement.setString(4, account.getType().name());
            statement.setString(5, account.getCurrency().getId());
            statement.setString(6, account.getBalance().toString());
            statement.executeUpdate();
        }
    }

    public void delete(UUID accountId) {

        String sql = "DELETE FROM accounts WHERE id = ?";

        Connection connection = databaseManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountId.toString());
            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Could not delete account", e);

        }

    }

    private Account loadAccount(ResultSet result) throws SQLException {

        String currencyId = result.getString("currency");
        Currency currency = currencyManager.get(currencyId);

        if (currency == null) {
            // Fallback por si la moneda ya no existe en el registro
            currency = new Currency(currencyId, currencyId, "?");
        }

        return new Account(

                UUID.fromString(result.getString("id")), UUID.fromString(result.getString("owner_id")), result.getString("name"), AccountType.valueOf(result.getString("type")), currency, new BigDecimal(result.getString("balance"))

        );

    }

}