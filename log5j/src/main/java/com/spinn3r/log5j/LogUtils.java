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

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Date;

import com.spinn3r.tracepoint.Tracepoint;

public class LogUtils {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static String toString(LogEvent logEvent) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(baos, UTF8));

        writer.append(logEvent.level().name());
        writer.append(" [");
        writer.append(new Date(logEvent.time()).toString());
        writer.append("]: ");
        writer.append(logEvent.threadName());
        writer.append(" > ");
        writer.append(logEvent.logName());
        writer.append(" - ");
        writer.append(logEvent.message());
        if (logEvent.throwable() != null) {

            writer.append('\n');

            Tracepoint tp = new Tracepoint( logEvent.throwable() );
            writer.append( tp.toString() );
            
        }
        writer.close();

        return baos.toString();
    }

    private LogUtils() {
    }
}
