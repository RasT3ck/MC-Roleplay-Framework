package org.rast3ck.mcrp.core.task;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TaskManager {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<UUID, MCRPTask> tasks = new HashMap<>();

    public TaskHandle run(Runnable runnable) {

        return runLater(runnable, 0);

    }

    public TaskHandle runLater(Runnable runnable, long delay) {

        TaskHandle handle = new TaskHandle(UUID.randomUUID());

        MCRPTask task = new MCRPTask(handle, runnable, delay, 0, false);

        tasks.put(handle.getId(), task);

        return handle;

    }

    public TaskHandle runRepeating(Runnable runnable, long delay, long period) {

        TaskHandle handle = new TaskHandle(UUID.randomUUID());

        MCRPTask task = new MCRPTask(handle, runnable, delay, period, true);

        tasks.put(handle.getId(), task);

        return handle;

    }

    public void cancel(TaskHandle handle) {

        MCRPTask task = tasks.get(handle.getId());

        if (task != null) {

            task.cancel();

        }

    }

    public void cancelAll() {

        tasks.values().forEach(MCRPTask::cancel);

    }

    public boolean isRunning(TaskHandle handle) {

        MCRPTask task = tasks.get(handle.getId());

        return task != null && !task.isCancelled();

    }

    public void tick() {

        Iterator<MCRPTask> iterator = tasks.values().iterator();

        while (iterator.hasNext()) {

            MCRPTask task = iterator.next();

            if (task.isCancelled()) {

                iterator.remove();

                continue;

            }

            if (task.getDelay() > 0) {

                task.setDelay(task.getDelay() - 1);

                continue;

            }

            try {
                task.getRunnable().run();
            } catch (Exception e) {
                LOGGER.error("Error executing task {}", task.getHandle().getId(), e);
            }

            if (task.isRepeating()) {

                task.setDelay(task.getPeriod());

            } else {

                iterator.remove();

            }

        }

    }

}