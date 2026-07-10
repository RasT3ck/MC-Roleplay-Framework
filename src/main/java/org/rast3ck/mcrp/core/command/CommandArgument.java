package org.rast3ck.mcrp.core.command;

public class CommandArgument {

    private final String name;

    private final boolean required;

    public CommandArgument(
            String name,
            boolean required
    ) {

        this.name = name;
        this.required = required;

    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

}