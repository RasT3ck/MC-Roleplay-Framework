package org.rast3ck.mcrp.core.economy;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequest {

    private final UUID actorId;

    private final Account fromAccount;
    private final Account toAccount;

    private final BigDecimal amount;

    private final TransactionType type;

    private final String reason;

    public TransactionRequest(UUID actorId,
                              Account fromAccount,
                              Account toAccount,
                              BigDecimal amount,
                              TransactionType type,
                              String reason) {

        this.actorId = actorId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.reason = reason;

    }

    public UUID getActorId() {
        return actorId;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

}