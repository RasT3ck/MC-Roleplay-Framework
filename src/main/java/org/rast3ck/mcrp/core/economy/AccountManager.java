package org.rast3ck.mcrp.core.economy;

import org.rast3ck.mcrp.core.economy.storage.AccountStorage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class AccountManager {

    private final Map<UUID, Account> accountsById = new LinkedHashMap<>();

    private final Map<UUID, Map<UUID, Account>> accountsByOwner = new LinkedHashMap<>();

    private final AccountStorage storage;

    public AccountManager(AccountStorage storage) {
        this.storage = storage;
    }

    public Account createAccount(UUID ownerId, String name, AccountType type, Currency currency) {

        return createAccount(ownerId, name, type, currency, BigDecimal.ZERO);

    }

    public Account createAccount(UUID ownerId, String name, AccountType type, Currency currency, BigDecimal initialBalance) {

        Account account = new Account(UUID.randomUUID(), ownerId, name, type, currency, initialBalance);


        storage.save(account);

        register(account);


        return account;

    }

    public void save(Account account) {
        storage.save(account);
    }

    public void save(Connection connection, Account account) throws SQLException {
        storage.save(connection, account);
    }

    public void register(Account account) {


        accountsById.put(account.getId(), account);


        accountsByOwner.computeIfAbsent(account.getOwnerId(), k -> new LinkedHashMap<>()).put(account.getId(), account);

    }

    public Account getAccount(UUID accountId) {

        return accountsById.get(accountId);

    }

    public Account getAccountByOwner(UUID ownerId) {

        Map<UUID, Account> accounts = accountsByOwner.get(ownerId);

        if (accounts != null) {

            return accounts.values().stream().findFirst().orElse(null);

        }

        Account account = storage.findByOwner(ownerId);

        if (account != null) {
            register(account);
        }

        return account;

    }

    public Collection<Account> getAccountsByOwner(UUID ownerId) {


        Map<UUID, Account> accounts = accountsByOwner.get(ownerId);


        if (accounts == null) {

            return java.util.List.of();

        }


        return accounts.values();

    }

    public boolean hasAccount(UUID accountId) {

        return accountsById.containsKey(accountId);

    }

    public void removeAccount(UUID accountId) {


        Account account = accountsById.get(accountId);


        if (account == null) {

            return;

        }

        storage.delete(accountId);

        accountsById.remove(accountId);


        Map<UUID, Account> ownerAccounts = accountsByOwner.get(account.getOwnerId());


        if (ownerAccounts != null) {

            ownerAccounts.remove(accountId);

        }

    }

    public Collection<Account> getAccounts() {

        return accountsById.values();

    }

    /**
     * Libera del caché en memoria las cuentas de un jugador cuando se desconecta.
     * No borra nada de la base de datos, solo evita que queden retenidas indefinidamente en RAM.
     */
    public void unload(UUID ownerId) {

        Map<UUID, Account> accounts = accountsByOwner.remove(ownerId);

        if (accounts == null) {
            return;
        }

        for (UUID accountId : accounts.keySet()) {
            accountsById.remove(accountId);
        }

    }

}