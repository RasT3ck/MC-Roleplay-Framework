package org.rast3ck.mcrp.core.region.action.executor;

import net.minecraft.server.level.ServerPlayer;
import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.job.JobRank;
import org.rast3ck.mcrp.core.permission.PermissionManager;
import org.rast3ck.mcrp.core.region.action.RegionActionExecutor;

public class PermissionActionExecutor implements RegionActionExecutor {


    @Override
    public String getType() {

        return "permission";

    }



    @Override
    public void execute(
            ServerPlayer player,
            String data
    ) {


        String[] args =
                data.split(
                        ":",
                        2
                );


        if(args.length < 2)
            return;



        String action =
                args[0];


        String permission =
                args[1];



        PermissionManager manager =
                MCRoleplayFramework.getCore()
                        .getPermissionManager();



        JobRank rank =
                MCRoleplayFramework.getCore()
                        .getJobManager()
                        .getPlayerRank(
                                player.getUUID()
                        );



        if(rank == null)
            return;



        switch(action) {


            case "add" -> {

                manager.addPermission(
                        rank.getId(),
                        permission
                );

            }


            case "remove" -> {

                manager.removePermission(
                        rank.getId(),
                        permission
                );

            }

        }

    }

}