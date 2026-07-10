package org.rast3ck.mcrp.core.job.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.MCRPConstants;
import org.rast3ck.mcrp.core.config.MCRPConfig;
import org.rast3ck.mcrp.core.economy.Account;
import org.rast3ck.mcrp.core.economy.TransactionRequest;
import org.rast3ck.mcrp.core.economy.TransactionType;
import org.rast3ck.mcrp.core.job.Job;

@Mod.EventBusSubscriber(modid = MCRPConstants.MOD_ID)
public final class SalaryScheduler {

    private static final int CHECK_INTERVAL_TICKS = 20 * 60;
    private static int ticks;

    private SalaryScheduler() {
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || ++ticks < CHECK_INTERVAL_TICKS) {
            return;
        }

        ticks = 0;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            processPlayer(player);
        }
    }

    private static void processPlayer(ServerPlayer player) {
        var core = MCRoleplayFramework.getCore();

        Job job = core.getJobManager().getPlayerJobDefinition(player.getUUID());
        if (job == null || job.getSalary().signum() <= 0) {
            return;
        }

        Account account = core.getEconomyManager().getAccountManager().getAccountByOwner(player.getUUID());
        if (account == null) {
            return;
        }

        int payments = core.getJobManager().recordWork(player.getUUID(), 60, MCRPConfig.SALARY_INTERVAL_MINUTES * 60L);

        for (int payment = 0; payment < payments; payment++) {
            core.getEconomyManager().performTransaction(new TransactionRequest(null, null, account, job.getSalary(), TransactionType.SALARY, "Salary for " + job.getName()));
        }
    }
}
