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

public abstract class AbstractLoggable implements Loggable {

    private final String _logName;

    private boolean _async;

    private final InternalLogger _logger;

    public AbstractLoggable(String logName,
                            boolean async,
                            InternalLogger logger) {
        _logName = logName;
        _async = async;
        _logger = logger;
    }

    protected void log(LogEvent logEvent) {
        if (_logger.isEnabled(logEvent.level())) {
            if (_async) {
                LogManager.logAsync(logEvent);
            } else {
                logEvent.logger().log(logEvent);
            }
        }
    }

    protected InternalLogger internalLogger() {
        return _logger;
    }

    public String getName() {
        return _logName;
    }

    public void trace(String formatMessage, Object... params) {
        if (_logger.isEnabled(LogLevel.TRACE)) {
            log(LogEvent.create(_logger, _logName, LogLevel.TRACE,
                    formatMessage, params));
        }
    }

    public void debug(String formatMessage, Object... params) {
        if (_logger.isEnabled(LogLevel.DEBUG)) {
            log(LogEvent.create(_logger, _logName, LogLevel.DEBUG,
                    formatMessage, params));
        }
    }

    public void debug(String formatMessage, Throwable t, Object... params) {
        if (_logger.isEnabled(LogLevel.DEBUG)) {
            log(LogEvent.create(_logger, _logName, LogLevel.DEBUG,
                    formatMessage, params, t));
        }
    }

    public void info(String formatMessage, Object... params) {
        if (_logger.isEnabled(LogLevel.INFO)) {
            log(LogEvent.create(_logger, _logName, LogLevel.INFO,
                    formatMessage, params));
        }
    }

    public void info(String formatMessage, Throwable t, Object... params) {
        if (_logger.isEnabled(LogLevel.INFO)) {
            log(LogEvent.create(_logger, _logName, LogLevel.INFO,
                    formatMessage, params, t));
        }
    }

    public void warn(String formatMessage, Object... params) {
        if (_logger.isEnabled(LogLevel.WARN)) {
            if (params != null && params.length > 0
                    && params[0] instanceof Throwable) {
                warn(formatMessage, (Throwable) params[0], withoutFirst(params));
            } else {
                log(LogEvent.create(_logger, _logName, LogLevel.WARN,
                        formatMessage, params));
            }
        }
    }

    public void warn(String formatMessage, Throwable t, Object... params) {
        if (_logger.isEnabled(LogLevel.WARN)) {
            log(LogEvent.create(_logger, _logName, LogLevel.WARN,
                    formatMessage, params, t));
        }
    }

    public void error(String formatMessage, Object... params) {
        if (_logger.isEnabled(LogLevel.ERROR)) {
            if (params != null && params.length > 0
                    && params[0] instanceof Throwable) {
                error(formatMessage, (Throwable) params[0], withoutFirst(params));
            } else {
                log(LogEvent.create(_logger, _logName, LogLevel.ERROR,
                        formatMessage, params));
            }
        }
    }

    public void error(String formatMessage, Throwable t, Object... params) {
        if (_logger.isEnabled(LogLevel.ERROR)) {
            log(LogEvent.create(_logger, _logName, LogLevel.ERROR,
                    formatMessage, params, t));
        }
    }

    public void fatal(String formatMessage, Object... params) {
        if (_logger.isEnabled(LogLevel.FATAL)) {
            if (params != null && params.length > 0
                    && params[0] instanceof Throwable) {
                fatal(formatMessage, (Throwable) params[0], withoutFirst(params));
            } else {
                log(LogEvent.create(_logger, _logName, LogLevel.FATAL,
                        formatMessage, params));
            }
        }
    }

    public void fatal(String formatMessage, Throwable t, Object... params) {
        if (_logger.isEnabled(LogLevel.FATAL)) {
            log(LogEvent.create(_logger, _logName, LogLevel.FATAL,
                    formatMessage, params, t));
        }
    }

    private static Object[] withoutFirst(Object... params) {
        Object[] newParams = new Object[params.length - 1];
        if (newParams.length > 0) {
            System.arraycopy(params, 1, newParams, 0, newParams.length);
        }
        return newParams;
    }

}
