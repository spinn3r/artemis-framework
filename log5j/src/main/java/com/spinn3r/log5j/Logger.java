/*
 * Copyright 2010 "Tailrank, Inc (Spinn3r)"
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.spinn3r.log5j;

/**
 * Logger facade that supports printf style message format for performance,
 * and ease of use, and easy construction support to determine the
 * category name by inspection.
 * <p/>
 * http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/Logger.html
 * <p/>
 * The log5j package supports a 'modernized' interface on top of the class Log4j
 * API usage.
 * <p/>
 * It provides a few syntactic extensions thanks to JDK 1.5 (hence the name
 * log5j).
 * <p/>
 * First.  It is no long required to give log4j the category when creating a new
 * class level logger.  Log5j just figures it out from the call stack.
 * <p/>
 * For example old usage was:
 * <p/>
 * private static final Logger log = Logger.getLogger( FeedTask.class );
 * <p/>
 * and the new syntax with Log5j:
 * <p/>
 * private static final Logger log = new Logger();
 * <p/>
 * Much better and fixes a lot of copy/paste errors.
 * <p/>
 * Second.  It provides sprintf support for logging messages
 * <p/>
 * Before:
 * <p/>
 * log.error( "This thing broke: " + foo + " due to bar: " + bar + " on this thing: " + car );
 * <p/>
 * After:
 * <p/>
 * log.error( "This thing broke: %s due to bar: %s on this thing: %s", foo, bar, car );
 * <p/>
 * That is SOOOOOO much better.  Good god!
 * <p/>
 * There's also a performance advantage here.
 * <p/>
 * If you were using log.debug() calls with string concat the strings are
 * CONSTANTLY generated even if the debug level is disabled.  This burns CPU and
 * pollutes your heap leading to addition garbage collection.
 * <p/>
 * Now internally the log.debug message isn't even called and the string is
 * never expanded/formatted unless the debug level is enabled.
 * <p/>
 * The log is asynchronous by default, but factory methods (Logger.getLogger)
 * support additional boolean parameter to control the behavior.
 */
public class Logger extends AbstractLoggable {

    public static boolean DEFAULT_ASYNC = true;
    
    protected Logger(String logName, boolean async) {
        super(logName, async, LogManager.createInternalLogger(logName));
    }

    protected Logger(String logName, boolean async, InternalLogger logger) {
        super(logName, async, logger);
    }

    // factories

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz, DEFAULT_ASYNC);
    }

    public static Logger getLogger(Class<?> clazz, boolean async) {
        return getLogger(clazz.getName(), async);
    }

    public static Logger getLogger() {
        return getLogger(getCallerClassName(), DEFAULT_ASYNC);
    }

    public static Logger getLogger(boolean async) {
        return getLogger(getCallerClassName(), async);
    }

    public static Logger getLogger(String name) {
        return getLogger(name, DEFAULT_ASYNC);
    }

    public static Logger getLogger(String name, boolean async) {
        return new Logger(name, async);
    }


    public boolean isDebugEnabled() {
        return internalLogger().isEnabled(LogLevel.DEBUG);
    }

    public boolean isInfoEnabled() {
        return internalLogger().isEnabled(LogLevel.INFO);
    }

    public boolean isWarnEnabled() {
        return internalLogger().isEnabled(LogLevel.WARN);
    }

    public boolean isErrorEnabled() {
        return internalLogger().isEnabled(LogLevel.ERROR);
    }

    public boolean isFatalEnabled() {
        return internalLogger().isEnabled(LogLevel.FATAL);
    }

    public void trace(java.lang.Object message) {
        super.trace(String.valueOf(message));
    }

    public void debug(java.lang.Object message) {
        super.debug(String.valueOf(message));
    }

    public void info(java.lang.Object message) {
        super.info(String.valueOf(message));
    }

    public void warn(java.lang.Object message) {
        super.warn(String.valueOf(message));
    }

    public void error(java.lang.Object message) {
        super.error(String.valueOf(message));
    }

    public void fatal(java.lang.Object message) {
        super.fatal(String.valueOf(message));
    }

    private static String getCallerClassName() {
        return new Exception().getStackTrace()[2].getClassName();
    }
}
