package com.spinn3r.artemis.test.http;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A default response which is just a POJO that we can manipulate.
 */
public class DefaultResponseGenerator implements ResponseGenerator {

    private Map<String,String> responseHeaders = new HashMap<>();

    private int responseCode = 200;

    private byte[] response;

    @Override
    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public ResponseGenerator setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
        return this;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    public ResponseGenerator setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    @Override
    public byte[] getResponse() throws IOException {
        return response;
    }

    public ResponseGenerator setResponse( String content ) {
        return setResponse( content.getBytes( Charsets.UTF_8 ) );
    }

    public ResponseGenerator setResponse(byte[] response) {
        this.response = response;
        return this;
    }

}
