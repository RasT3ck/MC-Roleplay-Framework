package org.rast3ck.mcrp.core.economy;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class PlayerAccountHandler {


    private final AccountManager accountManager;
    private final Currency currency;


    public PlayerAccountHandler(
            AccountManager accountManager,
            Currency currency
    ){

        this.accountManager = accountManager;
        this.currency = currency;

    }


    public void createAccount(ServerPlayer player){

        UUID uuid = player.getUUID();


        if(accountManager.getAccountByOwner(uuid) != null){

            return;

        }


        accountManager.createAccount(
                uuid,
                "Cuenta Personal",
                AccountType.PERSONAL,
                currency
        );

    }

}