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

import com.spinn3r.log5j.formatter.MessageFormatter;

import java.util.Arrays;

public class LogEvent {
    private static ThreadLocal<MessageFormatter> __tlMessageFormatter =
            new ThreadLocal<MessageFormatter>() {
                @Override
                protected MessageFormatter initialValue() {
                    return LogManager.__messageFormatterFactory.create();
                }
            };

    private final InternalLogger _internalLogger;

    private final long _time;

    private final String _threadName;

    private final String _logName;

    private final LogLevel _level;

    private final String _formatMessage;

    private final Object[] _params;

    private final Throwable _throwable;

    private LogEvent(String threadName,
                     InternalLogger internalLogger,
                     String logName,
                     LogLevel level,
                     String formatMessage,
                     Object[] params,
                     Throwable throwable) {
        _time = System.currentTimeMillis();
        _threadName = threadName;
        _internalLogger = internalLogger;
        _logName = logName;
        _level = level;
        _formatMessage = formatMessage;
        _params = params;
        _throwable = throwable;
    }

    public long time() {
        return _time;
    }

    public String threadName() {
        return _threadName;
    }

    protected InternalLogger logger() {
        return _internalLogger;
    }

    public String logName() {
        return _logName;
    }

    public LogLevel level() {
        return _level;
    }

    public String formatMessage() {
        return _formatMessage;
    }

    public Object[] params() {
        return _params;
    }

    public Throwable throwable() {
        return _throwable;
    }

    public String message() {
        if (_params == null || _params.length == 0) {
            return _formatMessage;
        } else {
            return __tlMessageFormatter.get().format(_formatMessage, _params);
        }
    }

    @Override
    public String toString() {
        return "LogEvent{" +
                "time=" + _time +
                ", threadName='" + _threadName + '\'' +
                ", level=" + _level +
                ", formatMessage='" + _formatMessage + '\'' +
                ", params=" + (_params == null ? null : Arrays.asList(_params)) +
                ", throwable=" + _throwable +
                '}';
    }

    public static LogEvent create(InternalLogger internalLogger, String logName,
            LogLevel level, String message) {
        return create(internalLogger, logName, level, message, null, null);
    }

    public static LogEvent create(InternalLogger internalLogger, String logName,
            LogLevel level, String message, Throwable throwable) {
        return create(internalLogger, logName, level, message, null, throwable);
    }

    public static LogEvent create(InternalLogger internalLogger, String logName,
            LogLevel level, String message, Object[] params) {
        return create(internalLogger, logName, level, message, params, null);
    }

    public static LogEvent create(InternalLogger internalLogger, String logName,
            LogLevel level, String message, Object[] params, Throwable throwable) {
        String threadName = Thread.currentThread().getName();
        return create(threadName, internalLogger, logName, level, message,
                params, throwable);
    }

    public static LogEvent create(
            String threadName,
            InternalLogger internalLogger,
            String logName,
            LogLevel level,
            String formatMessage,
            Object[] params,
            Throwable throwable) {
        return new LogEvent(threadName, internalLogger, logName, level,
                formatMessage, params, throwable);
    }
}
