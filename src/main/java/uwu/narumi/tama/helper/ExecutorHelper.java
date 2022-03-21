package uwu.narumi.tama.helper;

import uwu.narumi.tama.Tama;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public final class ExecutorHelper {

    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool(
            Integer.parseInt(Tama.INSTANCE.getProperties().getProperty("discord.bot.worker.threads"))
    );

    private ExecutorHelper() {
    }

    public static <T> T invoke(ForkJoinTask<T> task) {
        return FORK_JOIN_POOL.invoke(task);
    }

    public static void execute(ForkJoinTask<?> task) {
        FORK_JOIN_POOL.execute(task);
    }

    public static void execute(Runnable task) {
        FORK_JOIN_POOL.execute(task);
    }

    public static <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
        return FORK_JOIN_POOL.submit(task);
    }

    public static <T> ForkJoinTask<T> submit(Callable<T> task) {
        return FORK_JOIN_POOL.submit(task);
    }

    public static <T> ForkJoinTask<T> submit(Runnable task, T result) {
        return FORK_JOIN_POOL.submit(task, result);
    }

    public static ForkJoinTask<?> submit(Runnable task) {
        return FORK_JOIN_POOL.submit(task);
    }
}
