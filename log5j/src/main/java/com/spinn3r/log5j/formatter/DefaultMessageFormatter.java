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
package com.spinn3r.log5j.formatter;

import java.util.Formatter;
import java.util.Locale;

public class DefaultMessageFormatter implements MessageFormatter {
    private final Formatter _formatter;

    public DefaultMessageFormatter(Locale locale) {
        _formatter = new Formatter(locale);
    }

    public String format(String format, Object... args) {
        StringBuilder sb = (StringBuilder) _formatter.out();
        try {
            _formatter.format(format, args);
            return sb.toString();
        } finally {
            sb.setLength(0);
        }
    }
}
