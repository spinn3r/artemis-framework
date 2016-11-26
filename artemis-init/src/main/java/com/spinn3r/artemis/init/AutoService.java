package com.spinn3r.artemis.init;

/**
 * Simple service which, if implemented, has start() called for each instance
 * created. Note that this MUST be a {@link com.google.inject.Singleton}
 *
 * An AutoService is only started when getInstance is called on the injector.
 *
 * Additionally start() is only called when the launcher is calling launch()
 * and not init().
 */
public interface AutoService {

    void start() throws Exception;

    void stop() throws Exception;

}
