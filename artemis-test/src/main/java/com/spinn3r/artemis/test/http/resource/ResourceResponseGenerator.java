package com.spinn3r.artemis.test.http.resource;

import com.google.common.io.ByteStreams;
import com.spinn3r.artemis.test.http.ResponseGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ResourceResponseGenerator implements ResponseGenerator {

    private IOException cause;

    private int responseCode = 200;

    private byte[] response;

    public ResourceResponseGenerator(String path) {

        try ( InputStream is = getClass().getResourceAsStream( path ) ) {

            response = ByteStreams.toByteArray( is );

        } catch ( IOException e ) {
            responseCode = 404;
            cause = e;
        }


    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return new HashMap<>();
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public byte[] getResponse() throws IOException {

        if ( cause != null )
            throw cause;

        return response;
    }

}
