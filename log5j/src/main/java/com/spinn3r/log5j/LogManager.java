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
import com.spinn3r.log5j.formatter.DefaultMessageFormatterFactory;
import com.spinn3r.log5j.formatter.MessageFormatterFactory;

public class LogManager {
    public static final String FACTORY_CLASS_NAME = "log5j.factory";

    public static final String FORMATTER_FACTORY_CLASS_NAME = "log5j.formatter.factory";

    private static final AsyncLogger __asyncLogger = new AsyncLogger();

    private static InternalLoggerFactory __internalLoggerFactory;

    static MessageFormatterFactory __messageFormatterFactory;

    private static volatile boolean _disableShutdownHook = false;

    private static volatile boolean _shuttingDown = false;
    
    static {
        try {
            String factory = System.getProperty(FACTORY_CLASS_NAME,
                    "com.spinn3r.log5j.factories.Log4jInternalLoggerFactory");
            Class<?> clazz = Class.forName(factory);
            __internalLoggerFactory = (InternalLoggerFactory) clazz.newInstance();
        } catch (Throwable e) {
            System.err.println("Cannot initialize internal logger factory:");
            e.printStackTrace();
            __internalLoggerFactory = new StdoutInternalLoggerFactory();
        }

        try {
            String factory = System.getProperty(FORMATTER_FACTORY_CLASS_NAME,
                    "com.spinn3r.log5j.formatter.DefaultMessageFormatterFactory");
            Class<?> clazz = Class.forName(factory);
            __messageFormatterFactory = (MessageFormatterFactory) clazz.newInstance();
        } catch (Throwable e) {
            System.err.println("Cannot initialize message formatter factory:");
            e.printStackTrace();
            __messageFormatterFactory = new DefaultMessageFormatterFactory();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                
                if ( _disableShutdownHook == false ) {
                    shutdown();
                }
                
            }
                
        } );
        
    }

    static InternalLogger createInternalLogger(String logName) {
        return __internalLoggerFactory.create(logName);
    }

    public static void log(LogEvent logEvent, boolean async) {
        if (logEvent.logger().isEnabled(logEvent.level())) {
            if ( async && _shuttingDown == false ) {
                logAsync(logEvent);
            } else {
                logEvent.logger().log(logEvent);
            }
        }
    }

    public static void logAsync(LogEvent logEvent) {
        if (logEvent.logger().isEnabled(logEvent.level())) {
            __asyncLogger.add(logEvent);
        }
    }

    public static void enableExplicitShutdown() {
        _disableShutdownHook = true;
    }

    public static void shutdown() {
        _shuttingDown = true;
        __asyncLogger.shutdown();
        __internalLoggerFactory.shutdown();
    }

}
