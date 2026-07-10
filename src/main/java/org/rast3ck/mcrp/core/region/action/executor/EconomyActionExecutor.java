package org.rast3ck.mcrp.core.region.action.executor;

import net.minecraft.server.level.ServerPlayer;
import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.economy.Account;
import org.rast3ck.mcrp.core.economy.AccountManager;
import org.rast3ck.mcrp.core.economy.EconomyManager;
import org.rast3ck.mcrp.core.economy.TransactionRequest;
import org.rast3ck.mcrp.core.economy.TransactionType;
import org.rast3ck.mcrp.core.region.action.RegionActionExecutor;

import java.math.BigDecimal;

public class EconomyActionExecutor implements RegionActionExecutor {


    @Override
    public String getType() {

        return "economy";

    }


    @Override
    public void execute(ServerPlayer player, String data) {


        String[] args = data.split(":", 2);


        if (args.length < 2) return;


        String action = args[0];


        BigDecimal amount = new BigDecimal(args[1]);


        EconomyManager economyManager = MCRoleplayFramework.getCore().getEconomyManager();


        AccountManager accountManager = economyManager.getAccountManager();


        Account account = accountManager.getAccountByOwner(player.getUUID());


        if (account == null) return;


        TransactionType type;

        Account fromAccount = null;
        Account toAccount = null;


        switch (action) {

            case "give" -> {
                type = TransactionType.ADMIN_GIVE;
                toAccount = account;
            }

            case "take" -> {
                type = TransactionType.ADMIN_REMOVE;
                fromAccount = account;
            }

            case "set" -> {
                type = TransactionType.ADMIN_SET;
                toAccount = account;
            }

            default -> {
                return;
            }

        }

        TransactionRequest request = new TransactionRequest(player.getUUID(), fromAccount, toAccount, amount, type, "Region action: " + action);

        economyManager.performTransaction(request);

    }

}