package org.rast3ck.mcrp.core.economy;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private final UUID id;

    /**
     * Dueño de la cuenta.
     * Puede ser un jugador, empresa, banco, organización, etc.
     */
    private final UUID ownerId;

    /**
     * Nombre visible de la cuenta.
     * Ejemplo:
     * - Cuenta Personal
     * - Banco Central
     * - Policía
     * - Bakery
     */
    private String name;

    private AccountType type;

    private Currency currency;

    private BigDecimal balance;

    public Account(UUID id, UUID ownerId, String name, AccountType type, Currency currency, BigDecimal balance) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.currency = currency;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}