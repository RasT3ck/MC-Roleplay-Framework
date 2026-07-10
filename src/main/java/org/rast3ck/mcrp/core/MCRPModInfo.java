package org.rast3ck.mcrp.core;

import net.minecraftforge.fml.ModList;

public final class MCRPModInfo {

    private MCRPModInfo() {
    }

    public static String getVersion() {
        return ModList.get()
                .getModContainerById(MCRPConstants.MOD_ID)
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("UNKNOWN");
    }
}