package org.rast3ck.mcrp.core.economy.listener;

import org.rast3ck.mcrp.core.economy.Account;
import org.rast3ck.mcrp.core.economy.AccountManager;
import org.rast3ck.mcrp.core.economy.AccountType;
import org.rast3ck.mcrp.core.economy.CurrencyManager;
import org.rast3ck.mcrp.core.event.EventBus;
import org.rast3ck.mcrp.core.event.player.MCRPPlayerJoinEvent;

import java.util.UUID;

public class EconomyPlayerListener {

    private final AccountManager accountManager;
    private final CurrencyManager currencyManager;

    public EconomyPlayerListener(
            EventBus eventBus,
            AccountManager accountManager,
            CurrencyManager currencyManager
    ) {

        this.accountManager = accountManager;
        this.currencyManager = currencyManager;

        eventBus
                .register(
                        MCRPPlayerJoinEvent.class,
                        this::onPlayerJoin
                );
    }

    private void onPlayerJoin(MCRPPlayerJoinEvent event) {

        UUID playerId = event.getPlayer().getUUID();

        Account account = accountManager.getAccountByOwner(playerId);

        if (account != null) {
            return;
        }

        accountManager.createAccount(
                playerId,
                "Cuenta Personal",
                AccountType.PERSONAL,
                currencyManager.getDefault()
        );

    }

}
