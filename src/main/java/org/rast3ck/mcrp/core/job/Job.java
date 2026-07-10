package org.rast3ck.mcrp.core.job;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Job {

    private final UUID id;

    private final String name;

    private final BigDecimal salary;


    public Job(
            UUID id,
            String name,
            BigDecimal salary
    ) {

        this.id = id;
        this.name = name;
        this.salary = salary;

    }


    public UUID getId() {

        return id;

    }


    public String getName() {

        return name;

    }


    public BigDecimal getSalary() {

        return salary;

    }

}