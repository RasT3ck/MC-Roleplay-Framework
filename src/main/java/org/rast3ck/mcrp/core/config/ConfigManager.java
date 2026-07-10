package org.rast3ck.mcrp.core.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {


    private final List<ConfigFile> configs =
            new ArrayList<>();


    public void register(ConfigFile config) {

        configs.add(config);

    }


    public void loadAll() {

        configs.forEach(
                ConfigFile::load
        );

    }


    public void saveAll() {

        configs.forEach(
                ConfigFile::save
        );

    }

}