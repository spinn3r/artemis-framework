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

public interface Log {
    boolean enabled(LogLevel level);

    void t(String formatMessage, Object... formatParams);

    void t(Throwable t, String formatMessage, Object... formatParams);

    void d(String formatMessage, Object... formatParams);

    void d(Throwable t, String formatMessage, Object... formatParams);

    void i(String formatMessage, Object... formatParams);

    void i(Throwable t, String formatMessage, Object... formatParams);

    void w(String formatMessage, Object... formatParams);

    void w(Throwable t, String formatMessage, Object... formatParams);

    void e(String formatMessage, Object... formatParams);

    void e(Throwable t, String formatMessage, Object... formatParams);

    void f(String formatMessage, Object... formatParams);

    void f(Throwable t, String formatMessage, Object... formatParams);
}
