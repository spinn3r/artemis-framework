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
package com.spinn3r.log5j.factories;

import com.spinn3r.log5j.InternalLogger;
import com.spinn3r.log5j.InternalLoggerFactory;
import com.spinn3r.log5j.LogEvent;
import com.spinn3r.log5j.LogLevel;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;

import java.util.EnumMap;

// be carefull with LoggingEvent in custom appenders (lazily initialized
// ThrowableInformation won't be correct if it's used in asynchronous mode)

public class Log4jInternalLoggerFactory implements InternalLoggerFactory {
    private static final EnumMap<LogLevel, Level> __levelMapping;

    static {
        __levelMapping = new EnumMap<LogLevel, Level>(LogLevel.class);
        __levelMapping.put(LogLevel.TRACE, Level.TRACE);
        __levelMapping.put(LogLevel.DEBUG, Level.DEBUG);
        __levelMapping.put(LogLevel.INFO, Level.INFO);
        __levelMapping.put(LogLevel.WARN, Level.WARN);
        __levelMapping.put(LogLevel.ERROR, Level.ERROR);
        __levelMapping.put(LogLevel.FATAL, Level.FATAL);
    }

    public Log4jInternalLoggerFactory() {
    }

    public InternalLogger create(String logName) {
        final Logger logger = Logger.getLogger(logName);
        return new InternalLogger() {
            public boolean isEnabled(LogLevel level) {
                return logger.isEnabledFor(toLog4JLevel(level));
            }

            public void log(LogEvent event) {
                Throwable t = event.throwable();
                LoggingEvent loggingEvent = new InternalLoggingEvent(logger,
                                                                     event.threadName(),
                                                                     event.time(),
                                                                     toLog4JLevel(event.level()),
                                                                     event.message(),
                                                                     t);
                logger.callAppenders(loggingEvent);
            }
        };
    }

    public void shutdown() {
        LogManager.shutdown();
    }

    private Level toLog4JLevel(LogLevel level) {
        return __levelMapping.get(level);
    }

    static class InternalLoggingEvent extends LoggingEvent {
        private final String _threadName;

        InternalLoggingEvent(Category category,
                             String threadName,
                             long eventTime,
                             Priority priority,
                             Object o,
                             Throwable throwable) {
            super("", category, eventTime, priority, o, throwable);
            
            _threadName = threadName;
        }

        @Override
        public String getThreadName() {
            return _threadName;
        }
    }
}
