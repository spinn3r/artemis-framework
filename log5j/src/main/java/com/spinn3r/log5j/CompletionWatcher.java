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
 * Tool to watch for the completion of a method an warn if it takes too long.
 * This is somewhat expensive to use in that internally it uses a Thread.
 * <p/>
 * For monitoring a pool of objects it would be ideal though especially for
 * something like a JDBC database connection which might be taking too long to
 * complete.
 * <p/>
 * TODO: refactor to NOT expose Thread semantics to callers.
 *
 * @version $Id: $
 */
public class CompletionWatcher extends Thread {
    public static boolean DISABLED = false;

    private final long _duration;

    private final Log _log;

    private final Logger _logger;

    private final String _message;

    private final Object[] _args;

    private final Thread _parentThread;

    private DelayedTask _task;

    public CompletionWatcher(Logger logger, String message, Object... args) {
        this(1000, null, logger, message, args);
    }

    public CompletionWatcher(Logger logger, long duration,
            String message, Object... args) {
        this(duration, null, logger, message, args);
    }

    public CompletionWatcher(Log log, String message, Object... args) {
        this(1000, log, null, message, args);
    }

    public CompletionWatcher(Log log, long duration,
            String message, Object... args) {
        this(duration, log, null, message, args);
    }

    private CompletionWatcher(long duration, Log log, Logger logger,
            String message, Object[] args) {
        _duration = duration;
        _log = log;
        _logger = logger;
        _message = message;
        _args = args;
        _parentThread = Thread.currentThread();
    }

    public void start() {
        if (!DISABLED) {
            _task = new DelayedTask(_duration) {
                @Override
                public void run() {

                    String logMessage = String.format(_message + "\n", _args) +
                            getOffendingThreadStackTrace();
                    if (_log != null) {
                        _log.w(logMessage);
                    } else if (_logger != null) {
                        _logger.warn(logMessage);
                    } else {
                        System.out.println(logMessage);
                    }
                }
            };
        }
    }

    public void complete() {
        if (!DISABLED && _task != null) {
            _task.cancel();
            _task = null;
        }
    }

    private String getOffendingThreadStackTrace() {

        StringBuilder buff = new StringBuilder(2500);

        StackTraceElement[] stack = _parentThread.getStackTrace();

        for (StackTraceElement frame : stack) {
            buff.append("\tat ");
            buff.append(frame);
            buff.append('\n');
        }

        return buff.toString();
    }

    public static void main(String[] args) throws Exception {

        Logger log = Logger.getLogger();
        String name = "main";
        CompletionWatcher watcher = new CompletionWatcher(
                log, "Test took too long to complete for test %s", name);

        try {
            watcher.start();
            Thread.sleep(200);
        } finally {
            watcher.complete();
        }

        Thread.sleep(1000);
    }
}
