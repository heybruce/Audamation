package utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogWriter {
    private LogWriter() {
        throw new IllegalStateException("Utility class");
    }

    public static void write(Class clazz, Loggable.Level logLevel, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);

        switch (logLevel) {
            case TRACE:
                logger.trace(message);
                break;
            case DEBUG:
                logger.debug(message);
                break;
            case INFO:
                logger.info(message);
                break;
            case WARN:
                logger.warn(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            default:
                logger.warn("No suitable log level found");
                break;
        }
    }

}