package org.rast3ck.mcrp.core.job;

import java.util.UUID;

public class PlayerJob {

    private final UUID playerId;

    private final UUID jobId;

    private UUID rankId;

    private boolean onDuty;

    private long workedSeconds;


    public PlayerJob(
            UUID playerId,
            UUID jobId,
            UUID rankId
    ) {

        this.playerId = playerId;
        this.jobId = jobId;
        this.rankId = rankId;
        this.onDuty = false;
        this.workedSeconds = 0;

    }


    public UUID getPlayerId() {

        return playerId;

    }


    public UUID getJobId() {

        return jobId;

    }


    public UUID getRankId() {

        return rankId;

    }


    public void setRankId(UUID rankId) {

        this.rankId = rankId;

    }

    public boolean isOnDuty() {
        return onDuty;
    }

    public void setOnDuty(boolean onDuty) {
        this.onDuty = onDuty;
    }

    public long getWorkedSeconds() {
        return workedSeconds;
    }

    public void setWorkedSeconds(long workedSeconds) {
        this.workedSeconds = Math.max(0, workedSeconds);
    }

}
