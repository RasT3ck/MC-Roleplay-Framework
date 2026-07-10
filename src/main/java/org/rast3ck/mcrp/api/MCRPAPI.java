package org.rast3ck.mcrp.api;

import org.rast3ck.mcrp.api.economy.EconomyAPI;
import org.rast3ck.mcrp.api.module.ModuleAPI;
import org.rast3ck.mcrp.api.player.PlayerAPI;

public final class MCRPAPI {

    private MCRPAPI() {

    }

    public static PlayerAPI player() {
        return PlayerAPI.INSTANCE;
    }

    public static ModuleAPI modules() {
        return ModuleAPI.INSTANCE;
    }

    public static EconomyAPI economy() {
        return EconomyAPI.INSTANCE;
    }
}