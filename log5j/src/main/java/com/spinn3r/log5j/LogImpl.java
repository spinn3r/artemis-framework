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

public class LogImpl implements Log {
    private final String _logName;

    private final boolean _async;

    private final InternalLogger _logger;

    public LogImpl(String logName, boolean async, InternalLogger logger) {
        _logName = logName;
        _async = async;
        _logger = logger;
    }

    public boolean enabled(LogLevel level) {
        return _logger.isEnabled(level);
    }

    public void t(String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.TRACE)) {
            log(LogEvent.create(_logger, _logName, LogLevel.TRACE,
                    formatMessage, formatParams));
        }
    }

    public void t(Throwable t, String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.TRACE)) {
            log(LogEvent.create(_logger, _logName, LogLevel.TRACE,
                    formatMessage, formatParams, t));
        }
    }

    public void d(String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.DEBUG)) {
            log(LogEvent.create(_logger, _logName, LogLevel.DEBUG,
                    formatMessage, formatParams));
        }
    }

    public void d(Throwable t, String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.DEBUG)) {
            log(LogEvent.create(_logger, _logName, LogLevel.DEBUG,
                    formatMessage, formatParams, t));
        }
    }

    public void i(String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.INFO)) {
            log(LogEvent.create(_logger, _logName, LogLevel.INFO,
                    formatMessage, formatParams));
        }
    }

    public void i(Throwable t, String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.INFO)) {
            log(LogEvent.create(_logger, _logName, LogLevel.INFO,
                    formatMessage, formatParams, t));
        }
    }

    public void w(String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.WARN)) {
            log(LogEvent.create(_logger, _logName, LogLevel.WARN,
                    formatMessage, formatParams));
        }
    }

    public void w(Throwable t, String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.WARN)) {
            log(LogEvent.create(_logger, _logName, LogLevel.WARN,
                    formatMessage, formatParams, t));
        }
    }

    public void e(String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.ERROR)) {
            log(LogEvent.create(_logger, _logName, LogLevel.ERROR,
                    formatMessage, formatParams));
        }
    }

    public void e(Throwable t, String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.ERROR)) {
            log(LogEvent.create(_logger, _logName, LogLevel.ERROR,
                    formatMessage, formatParams, t));
        }
    }

    public void f(String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.FATAL)) {
            log(LogEvent.create(_logger, _logName, LogLevel.FATAL,
                    formatMessage, formatParams));
        }
    }

    public void f(Throwable t, String formatMessage, Object... formatParams) {
        if (enabled(LogLevel.FATAL)) {
            log(LogEvent.create(_logger, _logName, LogLevel.FATAL,
                    formatMessage, formatParams, t));
        }
    }

    private void log(LogEvent logEvent) {
        LogManager.log(logEvent, _async);
    }
}
