package com.spinn3r.artemis.init.tracer;

/**
 * Tracing infra so that we can communicate to the outside world during init.
 */
public interface Tracer {

    public void info( String format, Object ... args );

    public void warn( String format, Object ... args );

    public void error( String format, Object ... args );

    public void error( String format, Throwable throwable, Object ... args );

}
