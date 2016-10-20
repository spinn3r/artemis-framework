package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.URLResourceRequest;
import org.junit.Test;

import javax.net.ssl.SSLException;

import static com.spinn3r.artemis.network.builder.HttpRequestExecutor.isTransientHttpException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class HttpRequestExecutorTest {

    @Test
    public void testIsTransientHttpException() {

        assertTrue(isTransientHttpException(new NetworkException("", 503)));
        assertTrue(isTransientHttpException(new NetworkException("", URLResourceRequest.STATUS_CONNECT_TIMEOUT)));
        assertTrue(isTransientHttpException(new NetworkException("", URLResourceRequest.STATUS_READ_TIMEOUT)));
        assertTrue(isTransientHttpException(new SSLException("")));
        assertTrue(isTransientHttpException(new RuntimeException(new NetworkException("", 503))));

        assertFalse(isTransientHttpException(new NetworkException("", 404)));
        assertFalse(isTransientHttpException(new RuntimeException()));
        assertFalse(isTransientHttpException(new RuntimeException(new NetworkException("", 404))));


    }


}
