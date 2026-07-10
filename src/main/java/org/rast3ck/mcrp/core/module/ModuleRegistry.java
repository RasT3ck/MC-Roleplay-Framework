package org.rast3ck.mcrp.core.module;

import java.util.ArrayList;
import java.util.List;

public final class ModuleRegistry {

    private ModuleRegistry() {

    }


    private static final List<MCRPModule> MODULES =
            new ArrayList<>();


    public static void register(MCRPModule module) {

        MODULES.add(module);

    }


    public static List<MCRPModule> getModules() {

        return MODULES;

    }

}