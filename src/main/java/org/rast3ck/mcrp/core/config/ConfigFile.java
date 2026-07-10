package org.rast3ck.mcrp.core.config;

public interface ConfigFile {

    String getName();

    void load();

    void save();

}