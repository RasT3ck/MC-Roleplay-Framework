package org.rast3ck.mcrp.core.job;

import org.rast3ck.mcrp.core.job.storage.JobStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class JobManager {

    private final Map<UUID, Job> jobs = new LinkedHashMap<>();

    private final Map<UUID, PlayerJob> playerJobs = new LinkedHashMap<>();

    private final JobStorage storage;

    public JobManager(JobStorage storage) {
        this.storage = storage;
    }

    public void register(Job job) {
        storage.saveJob(job);
        jobs.put(job.getId(), job);
    }

    public Job getJob(UUID id) {
        return jobs.get(id);
    }

    public Collection<Job> getJobs() {
        return jobs.values();
    }

    public void assignJob(UUID playerId, Job job, JobRank rank) {

        PlayerJob playerJob = new PlayerJob(playerId, job.getId(), rank.getId());

        playerJobs.put(playerId, playerJob);

        storage.savePlayerJob(playerJob);
    }

    public PlayerJob getPlayerJob(UUID playerId) {


        PlayerJob playerJob = playerJobs.get(playerId);


        if (playerJob != null) {

            return playerJob;

        }


        playerJob = storage.findPlayerJob(playerId);


        if (playerJob != null) {

            playerJobs.put(playerId, playerJob);

        }


        return playerJob;

    }

    public void removeJob(UUID playerId) {
        storage.deletePlayerJob(playerId);
        playerJobs.remove(playerId);
    }

    public void changeRank(UUID playerId, JobRank rank) {

        PlayerJob playerJob = getPlayerJob(playerId);


        if (playerJob == null) {

            return;

        }


        playerJob.setRankId(rank.getId());

        storage.savePlayerJob(playerJob);

    }

    public boolean toggleDuty(UUID playerId) {
        PlayerJob playerJob = getPlayerJob(playerId);

        if (playerJob == null) {
            return false;
        }

        playerJob.setOnDuty(!playerJob.isOnDuty());
        storage.savePlayerJob(playerJob);
        return playerJob.isOnDuty();
    }

    public void setOffDuty(UUID playerId) {
        PlayerJob playerJob = getPlayerJob(playerId);

        if (playerJob != null && playerJob.isOnDuty()) {
            playerJob.setOnDuty(false);
            storage.savePlayerJob(playerJob);
        }
    }

    public int recordWork(UUID playerId, long elapsedSeconds, long salaryIntervalSeconds) {
        PlayerJob playerJob = getPlayerJob(playerId);

        if (playerJob == null || !playerJob.isOnDuty() || salaryIntervalSeconds <= 0) {
            return 0;
        }

        long total = playerJob.getWorkedSeconds() + elapsedSeconds;
        int payments = (int) (total / salaryIntervalSeconds);
        playerJob.setWorkedSeconds(total % salaryIntervalSeconds);
        storage.savePlayerJob(playerJob);
        return payments;
    }

    public Job getPlayerJobDefinition(UUID playerId) {

        PlayerJob playerJob = getPlayerJob(playerId);


        if (playerJob == null) {

            return null;

        }


        return storage.findJob(playerJob.getJobId());

    }

    public JobRank getPlayerRank(UUID playerId) {

        PlayerJob playerJob = getPlayerJob(playerId);


        if (playerJob == null) {

            return null;

        }


        return storage.findRank(playerJob.getRankId());

    }

    /**
     * Libera del caché en memoria el trabajo de un jugador cuando se desconecta.
     * No borra nada de la base de datos, solo evita que quede retenido en RAM.
     */
    public void unload(UUID playerId) {
        playerJobs.remove(playerId);
    }

}