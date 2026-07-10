package org.rast3ck.mcrp.api.module;

import org.rast3ck.mcrp.MCRoleplayFramework;
import org.rast3ck.mcrp.core.module.MCRPModule;

public final class ModuleAPI {

    public static final ModuleAPI INSTANCE =
            new ModuleAPI();


    private ModuleAPI() {

    }


    public void register(MCRPModule module) {

        MCRoleplayFramework.getCore()
                .getModuleManager()
                .register(module);

    }


    public MCRPModule get(String id) {

        return MCRoleplayFramework.getCore()
                .getModuleManager()
                .getModule(id);

    }


    public boolean exists(String id) {

        return MCRoleplayFramework.getCore()
                .getModuleManager()
                .hasModule(id);

    }

}