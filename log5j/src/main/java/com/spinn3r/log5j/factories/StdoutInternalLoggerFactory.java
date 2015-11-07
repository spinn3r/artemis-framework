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

import com.spinn3r.log5j.*;

public class StdoutInternalLoggerFactory implements InternalLoggerFactory {
    private static final InternalLogger LOGGER = new InternalLogger() {
        public boolean isEnabled(LogLevel level) {
            return true;
        }

        public void log(LogEvent logEvent) {
            System.out.println(LogUtils.toString(logEvent));
        }
    };

    public InternalLogger create(String logName) {
        return LOGGER;
    }

    public void shutdown() {
        // do nothing
    }
}
