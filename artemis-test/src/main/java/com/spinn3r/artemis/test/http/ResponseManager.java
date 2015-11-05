package com.spinn3r.artemis.test.http;

/**
 *
 */
public interface ResponseManager {

    /**
     * Get a response on a given path.
     *
     * @param path
     * @return
     */
    public ResponseGenerator getResponseGenerator(String path);

    public void path(String path, ResponseGenerator responseGenerator);

    public void path(String path, String content);

}
