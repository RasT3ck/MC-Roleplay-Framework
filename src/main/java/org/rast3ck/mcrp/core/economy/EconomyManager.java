package org.rast3ck.mcrp.core.economy;

import org.rast3ck.mcrp.core.data.database.DatabaseManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class EconomyManager {

    private final AccountManager accountManager;
    private final TransactionManager transactionManager;
    private final DatabaseManager databaseManager;

    public EconomyManager(AccountManager accountManager, TransactionManager transactionManager, DatabaseManager databaseManager) {

        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
        this.databaseManager = databaseManager;

    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public BigDecimal getBalance(Account account) {

        return account.getBalance();

    }

    public boolean performTransaction(TransactionRequest request) {

        if (request == null || request.getAmount() == null || request.getType() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        Account from = request.getFromAccount();
        Account to = request.getToAccount();

        if (!hasValidAccounts(request.getType(), from, to)) {
            return false;
        }

        if (from != null && to != null) {
            if (from.getCurrency() == null || to.getCurrency() == null || from.getId().equals(to.getId()) || !from.getCurrency().getId().equals(to.getCurrency().getId())) {
                return false;
            }
        }

        if (from != null && from.getBalance().compareTo(request.getAmount()) < 0 && removesBalance(request.getType())) {
            return false;
        }

        BigDecimal previousFrom = from == null ? null : from.getBalance();
        BigDecimal previousTo = to == null ? null : to.getBalance();

        Transaction transaction = new Transaction(UUID.randomUUID(), request.getActorId(), from == null ? null : from.getId(), to == null ? null : to.getId(), request.getAmount(), request.getType(), LocalDateTime.now(), request.getReason());

        // Sincronizamos sobre la conexión compartida: como esta operación usa
        // una transacción real (autoCommit=false), ninguna otra operación debe
        // usar la misma conexión mientras esto está en curso.
        Connection connection = databaseManager.getConnection();

        synchronized (connection) {

            try {
                connection.setAutoCommit(false);

                try {
                    applyBalances(request.getType(), request.getAmount(), from, to);

                    if (from != null) {
                        accountManager.save(connection, from);
                    }

                    if (to != null) {
                        accountManager.save(connection, to);
                    }

                    transactionManager.save(connection, transaction);
                    connection.commit();
                    transactionManager.cache(transaction);
                    return true;

                } catch (SQLException | RuntimeException e) {
                    connection.rollback();
                    throw e;
                }

            } catch (SQLException | RuntimeException e) {

                if (from != null) {
                    from.setBalance(previousFrom);
                }

                if (to != null) {
                    to.setBalance(previousTo);
                }

                return false;

            } finally {

                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // Si esto falla, la conexión probablemente ya está en mal estado;
                    // DatabaseManager.getConnection() la reabrirá en la siguiente operación.
                }

            }

        }

    }

    private boolean hasValidAccounts(TransactionType type, Account from, Account to) {
        return switch (type) {
            case TRANSFER, PAYMENT, PURCHASE, TAX, FINE -> from != null && to != null;
            case SALARY -> to != null;
            case WITHDRAW, ADMIN_REMOVE -> from != null;
            case DEPOSIT, ADMIN_GIVE, ADMIN_SET -> to != null;
        };
    }

    private boolean removesBalance(TransactionType type) {
        return switch (type) {
            case TRANSFER, PAYMENT, PURCHASE, TAX, FINE, WITHDRAW, ADMIN_REMOVE -> true;
            case DEPOSIT, ADMIN_GIVE, ADMIN_SET, SALARY -> false;
        };
    }

    private void applyBalances(TransactionType type, BigDecimal amount, Account from, Account to) {
        switch (type) {
            case TRANSFER, PAYMENT, PURCHASE, TAX, FINE, WITHDRAW, ADMIN_REMOVE -> {
                if (from != null) {
                    from.setBalance(from.getBalance().subtract(amount));
                }
                if (to != null) {
                    to.setBalance(to.getBalance().add(amount));
                }
            }
            case DEPOSIT, ADMIN_GIVE, SALARY -> to.setBalance(to.getBalance().add(amount));
            case ADMIN_SET -> to.setBalance(amount);
        }

    }

}