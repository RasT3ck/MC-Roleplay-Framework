package org.rast3ck.mcrp.core.event;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.*;

public class EventBus {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<Class<? extends MCRPEvent>, List<EventListener<? extends MCRPEvent>>> listeners = new HashMap<>();

    public <T extends MCRPEvent> void register(Class<T> eventClass, EventListener<T> listener) {

        listeners.computeIfAbsent(eventClass, key -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T extends MCRPEvent> void post(T event) {

        List<EventListener<? extends MCRPEvent>> list = listeners.get(event.getClass());


        if (list == null) return;


        for (EventListener<? extends MCRPEvent> listener : list) {

            try {
                ((EventListener<T>) listener).onEvent(event);
            } catch (Exception e) {
                LOGGER.error("Error handling event {}", event.getClass().getSimpleName(), e);
            }
        }
    }
}