package org.rast3ck.mcrp.core.registry;

import java.util.Collection;

public interface Registry<K, V> {

    void register(V value);

    void unregister(K key);

    V get(K key);

    boolean contains(K key);

    Collection<V> values();

    void clear();

}