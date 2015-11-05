package com.spinn3r.artemis.init.tracer;

import com.spinn3r.artemis.init.Service;

/**
 *
 */
public class StandardTracer implements Tracer {

    private Object service;

    public StandardTracer(Object service) {
        this.service = service;
    }

    @Override
    public void info(String format, Object... args) {
        System.out.printf( service.getClass().getName() + " INFO: " + format, args );
        System.out.printf( "\n" );
    }

    @Override
    public void warn(String format, Object... args) {
        System.err.printf( service.getClass().getName() + " WARN: " + format, args );
        System.err.printf( "\n" );
    }

    @Override
    public void error(String format, Object... args) {
        System.err.printf( service.getClass().getName() + " ERROR: " + format, args );
        System.err.printf( "\n" );
    }

    @Override
    public void error(String format, Throwable throwable, Object... args) {
        System.err.printf( service.getClass().getName() + " ERROR: " + format, args );
        System.err.printf( "\n" );
        throwable.printStackTrace();
    }

}
