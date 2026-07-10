package org.rast3ck.mcrp.core.task;

import java.util.Objects;
import java.util.UUID;

public final class TaskHandle {

    private final UUID id;

    public TaskHandle(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof TaskHandle other)) {
            return false;
        }

        return id.equals(other.id);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TaskHandle{id=" + id + '}';
    }

}