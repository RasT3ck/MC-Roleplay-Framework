package org.rast3ck.mcrp.core.economy;

import org.rast3ck.mcrp.core.registry.CurrencyRegistry;

import java.util.Collection;

public class CurrencyManager {

    private final CurrencyRegistry registry = new CurrencyRegistry();

    private Currency defaultCurrency;

    public void register(Currency currency) {
        registry.register(currency);
    }

    public Currency get(String id) {
        return registry.get(id);
    }

    public Collection<Currency> getCurrencies() {
        return registry.values();
    }

    public void setDefault(Currency currency) {
        this.defaultCurrency = currency;
        register(currency);
    }

    public Currency getDefault() {
        return defaultCurrency;
    }

}