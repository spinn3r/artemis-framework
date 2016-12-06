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

public class LogSink implements Loggable {

    private static final String LOG_NAME = "logsink";

    private final Loggable _loggables[];

    public String getName() {
        return LOG_NAME;
    }

    public LogSink(Loggable... loggables) {
        _loggables = new Loggable[loggables.length];
        for (int i = 0; i < loggables.length; ++i) {
            _loggables[i] = loggables[i];
        }
    }

    @Override
    public void debug(String formatMessage, Object... params) {
        for (Loggable loggable : _loggables) {
            loggable.debug(formatMessage, params);
        }
    }

    @Override
    public void info(String formatMessage, Object... params) {
        for (Loggable loggable : _loggables) {
            loggable.info(formatMessage, params);
        }
    }

    @Override
    public void warn(String formatMessage, Object... params) {
        for (Loggable loggable : _loggables) {
            loggable.warn(formatMessage, params);
        }
    }

    @Override
    public void error(String formatMessage, Object... params) {
        for (Loggable loggable : _loggables) {
            loggable.error(formatMessage, params);
        }
    }

    @Override
    public void fatal(String formatMessage, Object... params) {
        for (Loggable loggable : _loggables) {
            loggable.fatal(formatMessage, params);
        }
    }

    @Override
    public void fatal(String formatMessage, Throwable t) {
        fatal(formatMessage, t, toArray());
    }

    @Override
    public void fatal(String formatMessage, Throwable t, Object param0) {
        fatal(formatMessage, t, toArray(param0));
    }

    @Override
    public void fatal(String formatMessage, Throwable t, Object param0, Object param1) {
        fatal(formatMessage, t, toArray(param0, param1));
    }

    private void fatal(String formatMessage, Throwable t, Object... params) {
        for (Loggable loggable : _loggables) {
            loggable.fatal(formatMessage, t, params);
        }
    }

    @Override
    public void error(String formatMessage, Throwable t) {
        error(formatMessage, t, toArray());
    }

    @Override
    public void error(String formatMessage, Throwable t, Object param0) {
        error(formatMessage, t, toArray(param0));
    }

    @Override
    public void error(String formatMessage, Throwable t, Object param0, Object param1) {
        error(formatMessage, t, toArray(param0, param1));
    }

    private void error(String formatMessage, Throwable t, Object... params) {
        for (Loggable loggable : _loggables) {
            loggable.error(formatMessage, t, params);
        }
    }

    @Override
    public void warn(String formatMessage, Throwable t) {
        warn(formatMessage, t, toArray());
    }

    @Override
    public void warn(String formatMessage, Throwable t, Object param0) {
        warn(formatMessage, t, toArray(param0));
    }

    @Override
    public void warn(String formatMessage, Throwable t, Object param0, Object param1) {
        warn(formatMessage, t, toArray(param0, param1));
    }

    private void warn(String formatMessage, Throwable t, Object... params) {
        for (Loggable loggable : _loggables) {
            loggable.warn(formatMessage, t, params);
        }
    }
    
}
