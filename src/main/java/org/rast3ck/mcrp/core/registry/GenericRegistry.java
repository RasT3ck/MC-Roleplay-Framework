package org.rast3ck.mcrp.core.registry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class GenericRegistry<K, V>
        implements Registry<K, V> {

    private final Map<K, V> values =
            new LinkedHashMap<>();

    private final Function<V, K> keyProvider;

    public GenericRegistry(Function<V, K> keyProvider) {

        this.keyProvider = keyProvider;

    }

    @Override
    public void register(V value) {

        values.put(
                keyProvider.apply(value),
                value
        );

    }

    @Override
    public void unregister(K key) {

        values.remove(key);

    }

    @Override
    public V get(K key) {

        return values.get(key);

    }

    @Override
    public boolean contains(K key) {

        return values.containsKey(key);

    }

    @Override
    public Collection<V> values() {

        return values.values();

    }

    @Override
    public void clear() {

        values.clear();

    }

}