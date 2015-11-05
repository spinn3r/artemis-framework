package com.spinn3r.artemis.init.tracer;

import com.spinn3r.artemis.init.Service;
import org.apache.log4j.Logger;

/**
 * Tracer that uses log4j to initialize itself.
 */
public class Log4jTracer implements Tracer {

    private final Logger log;

    public Log4jTracer(Object service) {
        this.log = Logger.getLogger( service.getClass() );
    }

    @Override
    public void info(String format, Object... args) {
        log.info( String.format( format, args ) );
    }

    @Override
    public void warn(String format, Object... args) {
        log.warn( String.format( format, args ) );
    }

    @Override
    public void error(String format, Object... args) {
        log.error( String.format( format, args ) );
    }

    @Override
    public void error(String format, Throwable throwable, Object... args) {
        log.error( String.format( format, args ), throwable );
    }

}
