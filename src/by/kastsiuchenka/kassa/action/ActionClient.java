package by.kastsiuchenka.kassa.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class ActionClient {
    private static Logger logger = LogManager.getRootLogger();
    private static void sleepFor(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            logger.error("sleep has interrupted", e);
        }
    }
}
