package org.rast3ck.mcrp.core.task;

public class MCRPTask {

    private final TaskHandle handle;

    private final Runnable runnable;

    private long delay;

    private final long period;

    private final boolean repeating;

    private TaskState state;

    public MCRPTask(
            TaskHandle handle,
            Runnable runnable,
            long delay,
            long period,
            boolean repeating
    ) {

        this.handle = handle;
        this.runnable = runnable;
        this.delay = delay;
        this.period = period;
        this.repeating = repeating;
        this.state = TaskState.WAITING;

    }

    public TaskHandle getHandle() {
        return handle;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getPeriod() {
        return period;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public boolean isCancelled() {
        return state == TaskState.CANCELLED;
    }

    public void cancel() {
        this.state = TaskState.CANCELLED;
    }

}