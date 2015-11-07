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

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class DelayedTask extends TimerTask {

    private static final Log __log = LogFactory.getLog();

    private static final Timer _timer = new Timer("DelayedTaskTimer", true);

    private static final BlockingQueue<DelayedTaskOperation> __operations =
            new LinkedBlockingQueue<DelayedTaskOperation>();

    static {
        new OperationExecutorThread().start();
    }

    private final long _execTime;

    public DelayedTask() {
        this(1000);
    }

    public DelayedTask(long delay) {
        _execTime = System.currentTimeMillis() + delay;

        __operations.add(new DelayedTaskOperation(Operation.SCHEDULE, this));
    }

    static enum Operation {
        SCHEDULE, CANCEL;
    }

    static class DelayedTaskOperation {
        private final Operation _operation;
        private final DelayedTask _task;

        DelayedTaskOperation(Operation operation, DelayedTask task) {
            _operation = operation;
            _task = task;
        }

        public Operation operation() {
            return _operation;
        }

        public DelayedTask task() {
            return _task;
        }
    }

    static class OperationExecutorThread extends Thread {
        OperationExecutorThread() {
            super("DelayedTaskOperationExecutor");
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DelayedTaskOperation data = __operations.poll(3600, TimeUnit.SECONDS);
                    switch (data.operation()) {
                        case SCHEDULE:
                            _timer.schedule(data.task(),
                                    new Date(data.task()._execTime));
                            break;
                        case CANCEL:
                            data.task().cancel();
                            break;
                    }
                } catch (Throwable e) {
                    if (e.getMessage() != null && e.getMessage().contains(
                            "Task already scheduled or cancelled")) {
                        // that's possible when it's cancelled before scheduled
                        continue;
                    }
                    __log.w(e, "Unexpected exception");
                }
            }
        }
    }

}
