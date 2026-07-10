package org.rast3ck.mcrp.core.economy;

public enum AccountType {

    PERSONAL("Cuenta Personal"),
    BUSINESS("Empresa"),
    ORGANIZATION("Organización"),
    BANK("Banco");

    private final String defaultName;

    AccountType(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getDefaultName() {
        return defaultName;
    }
}
