package org.rast3ck.mcrp.core.economy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final UUID id;

    //Persona o sistema que ejecutó la transacción
    private final UUID actorId;

    private final UUID fromAccountId;
    private final UUID toAccountId;

    private final BigDecimal amount;

    private final TransactionType type;

    private final LocalDateTime date;

    private final String reason;

    public Transaction(UUID id, UUID actorId, UUID fromAccountId, UUID toAccountId, BigDecimal amount, TransactionType type, LocalDateTime date, String reason) {

        this.id = id;
        this.actorId = actorId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.reason = reason;

    }

    public UUID getId() {
        return id;
    }

    public UUID getActorId() {
        return actorId;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

}