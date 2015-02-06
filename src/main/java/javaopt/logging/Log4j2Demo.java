package javaopt.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Demo {

    private static final Logger logger = LogManager.getLogger("HelloWorld");

    public static void main(String[] args) {
        long start = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            if (logger.isInfoEnabled()) {
                logger.info("the log is {}, {}!", "Hello", "World");
            }
        }
        long end = System.nanoTime();
        System.out.println((end - start) / 1000000.0);
    }
}
