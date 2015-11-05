package com.spinn3r.artemis.test.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maintains a path from path to ResponseGenerator
 */
public class DefaultResponseManager implements ResponseManager {

    Map<String,ResponseGenerator> delegate = new ConcurrentHashMap<>();

    /**
     * Set the response generator for the given path.
     *
     * @param path
     * @param responseGenerator
     */
    public void path( String path, ResponseGenerator responseGenerator ) {
        delegate.put( path, responseGenerator );
    }

    public void path( String path, String content ) {

        path( path,
            new DefaultResponseGenerator()
                .setResponse( content ) );

    }

    public ResponseGenerator getResponseGenerator( String path ) {
        return delegate.get( path );
    }

}
