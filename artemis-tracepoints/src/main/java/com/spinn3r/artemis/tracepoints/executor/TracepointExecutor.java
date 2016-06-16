package com.spinn3r.artemis.tracepoints.executor;

/**
 *
 */
public class TracepointExecutor<T extends Throwable> extends BaseExecutor {

    public void execute(Executable<T> executable ) throws T {

        init();

        executable.execute();

    }

}

