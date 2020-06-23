package com.medallia.word2vec.util;

import com.google.common.base.Preconditions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.NullAppender;

/**
 * Creates loggers based on the caller's class.
 */
public final class AutoLog {
    /**
     * Prevents initialization.
     */
    private AutoLog() {
    }

    /**
     * @return {@link org.apache.commons.logging.Log} based on the caller's class
     */
    public static Log getLog() {
        return getLog(2);
    }

    /**
     * Make sure there is at least one appender to avoid a warning printed on stderr
     */
    private static class InitializeOnDemand {
        private static final boolean INIT = init();

        private static boolean init() {
            Logger rootLogger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
            if (rootLogger.getAppenders().isEmpty())
                rootLogger.addAppender(NullAppender.createAppender("Null Appender"));
            return true;
        }
    }

    /**
     * @return {@link org.apache.commons.logging.Log} based on the stacktrace distance to
     * the original caller. 1= the caller to this method. 2 = the caller to the caller... etc
     */
    public static Log getLog(int distance) {
        Preconditions.checkState(InitializeOnDemand.INIT);
        String callerClassName = Common.myCaller(distance).getClassName();
        try {
            return LogFactory.getLog(Class.forName(callerClassName));
        } catch (ClassNotFoundException t) {
            String err = "Class.forName on " + callerClassName + " failed";
            System.err.println(err);
            throw new IllegalStateException(err, t);
        }
    }
}
