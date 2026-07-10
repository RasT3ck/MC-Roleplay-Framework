package org.rast3ck.mcrp.core.manager;

public abstract class AbstractManager
        implements Initializable,
        Reloadable,
        Clearable,
        Closable {

    @Override
    public void initialize() {
    }

    @Override
    public void load() {
    }

    @Override
    public void reload() {

        clear();
        load();

    }

    @Override
    public void clear() {
    }

    @Override
    public void close() {
    }

}