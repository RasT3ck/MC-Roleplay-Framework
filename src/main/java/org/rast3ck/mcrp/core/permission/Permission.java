package org.rast3ck.mcrp.core.permission;

import java.util.UUID;

public class Permission {

    private final UUID id;

    private final String key;


    public Permission(
            UUID id,
            String key
    ) {

        this.id = id;
        this.key = key;

    }


    public UUID getId() {

        return id;

    }


    public String getKey() {

        return key;

    }

}