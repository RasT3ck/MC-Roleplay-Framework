package org.rast3ck.mcrp.core.registry;

import org.rast3ck.mcrp.core.economy.Currency;

public class CurrencyRegistry
        extends GenericRegistry<String, Currency> {

    public CurrencyRegistry() {

        super(Currency::getId);

    }

}
