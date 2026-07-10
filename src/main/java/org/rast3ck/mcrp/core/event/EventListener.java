package org.rast3ck.mcrp.core.event;

public interface EventListener<T extends MCRPEvent> {

    void onEvent(T event);

}