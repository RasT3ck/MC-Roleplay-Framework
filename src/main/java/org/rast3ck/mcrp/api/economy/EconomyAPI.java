package org.rast3ck.mcrp.api.economy;

import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.economy.Account;
import org.rast3ck.mcrp.core.economy.TransactionRequest;

import java.math.BigDecimal;

public final class EconomyAPI {

    public static final EconomyAPI INSTANCE =
            new EconomyAPI();


    private EconomyAPI() {

    }


    public BigDecimal getBalance(Account account) {

        return MCRoleplayFramework.getCore()
                .getEconomyManager()
                .getBalance(account);

    }


    public boolean transaction(TransactionRequest request) {

        return MCRoleplayFramework.getCore()
                .getEconomyManager()
                .performTransaction(request);

    }

}