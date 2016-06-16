package com.spinn3r.artemis.tracepoints.executor;

/**
 *
 */
public interface Executable<T extends Throwable> {

    void execute() throws T;

}
