package org.rast3ck.mcrp.core.economy;

import org.rast3ck.mcrp.core.economy.storage.TransactionStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    private final Map<UUID, Transaction> transactions = new LinkedHashMap<>();

    private final TransactionStorage storage;

    public TransactionManager(TransactionStorage storage) {
        this.storage = storage;
    }

    public void register(Transaction transaction) {
        storage.save(transaction);
        cache(transaction);
    }

    public void save(Connection connection, Transaction transaction) throws SQLException {
        storage.save(connection, transaction);
    }

    public void cache(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    public Transaction get(UUID id) {
        return transactions.get(id);
    }

    public Collection<Transaction> getTransactions() {
        return transactions.values();
    }
}
