package com.spinn3r.artemis.test.http;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public interface ResponseGenerator {

    /**
     * Include any response headers.
     * @return
     */
    public Map<String,String> getResponseHeaders();

    /**
     * Get the HTTP response code we should send.
     * @return
     */
    public int getResponseCode();

    /**
     * Response body.
     *
     * @return
     */
    public byte[] getResponse() throws IOException;

}
