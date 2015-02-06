package javaopt.logging.benchmark;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestLog4j2 {

    static final int THREAD_COUNT = Integer.parseInt(System.getProperty(
            "thread_count", "10"));
    static final long TIMEOUT = Integer.parseInt(System.getProperty("timeout",
            "60000"));
    static final long THREAD_AWAIT_TIMEOUT = Integer.parseInt(System
            .getProperty("thread_await_timeout", "60000"));

    static String packagePrefix = "javaopt.logging.benchmark.";

    public static boolean interrupted = false;

    public static void main(String[] args) throws Exception {
        String loggerName = args[0];
        String className = packagePrefix + loggerName;

        Class<Runnable> testClass = (Class<Runnable>) Class.forName(className);

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int j = 0; j < THREAD_COUNT; j++) {
            threadPool.execute(testClass.newInstance());
        }
        Thread.sleep(TIMEOUT);
        interrupted = true;

        threadPool.shutdown();
        if (!threadPool.awaitTermination(THREAD_AWAIT_TIMEOUT,
                TimeUnit.MILLISECONDS)) {
            threadPool.shutdownNow();
        }
    }

}
