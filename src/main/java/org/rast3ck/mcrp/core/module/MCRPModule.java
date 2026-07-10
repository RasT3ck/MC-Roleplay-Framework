package org.rast3ck.mcrp.core.module;

public interface MCRPModule {

    String getId();

    String getName();

    default void onInitialize() {

    }

    default void onShutdown() {

    }
}