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

import com.spinn3r.log5j.factories.StdoutInternalLoggerFactory;

public class StandardLogger extends AbstractLoggable {
    public StandardLogger(Loggable logger) {
        this(logger.getName());
    }

    public StandardLogger(String logName) {
        super(logName, true, new StdoutInternalLoggerFactory().create(logName));
    }
}
