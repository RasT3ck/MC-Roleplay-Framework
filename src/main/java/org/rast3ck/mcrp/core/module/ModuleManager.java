package org.rast3ck.mcrp.core.module;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleManager {

    private final Map<String, MCRPModule> modules = new LinkedHashMap<>();

    public void register(MCRPModule module) {

        modules.put(module.getId(), module);

    }

    public void initializeModules() {

        modules.values().forEach(MCRPModule::onInitialize);

    }

    public void shutdown() {

        modules.values().forEach(MCRPModule::onShutdown);

        modules.clear();

    }

    public boolean hasModule(String id) {

        return modules.containsKey(id);

    }

    public MCRPModule getModule(String id) {

        return modules.get(id);

    }

    public Collection<MCRPModule> getModules() {

        return modules.values();

    }

}