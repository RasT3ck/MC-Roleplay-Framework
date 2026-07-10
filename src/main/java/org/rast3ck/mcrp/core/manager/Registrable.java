package org.rast3ck.mcrp.core.manager;

public interface Registrable<T> {

    void register(T object);

    void unregister(T object);

}