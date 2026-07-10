package org.rast3ck.mcrp.core.job;

import java.util.UUID;

public class JobRank {

    private final UUID id;

    private final UUID jobId;

    private final String name;

    private final int level;


    public JobRank(
            UUID id,
            UUID jobId,
            String name,
            int level
    ) {

        this.id = id;
        this.jobId = jobId;
        this.name = name;
        this.level = level;

    }


    public UUID getId() {
        return id;
    }


    public UUID getJobId() {
        return jobId;
    }


    public String getName() {
        return name;
    }


    public int getLevel() {
        return level;
    }

}