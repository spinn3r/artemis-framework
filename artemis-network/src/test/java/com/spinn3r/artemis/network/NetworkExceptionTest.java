package com.spinn3r.artemis.network;

import org.junit.Assert;
import org.junit.Test;

public class NetworkExceptionTest {

    @Test
    public void testSimpleNE() {
        String message = "NPE message";
        NetworkException networkException = new NetworkException(message);

        Assert.assertEquals(message, networkException.getLocalizedMessage());
        Assert.assertEquals(-1, networkException.getResponseCode());
        Assert.assertFalse(networkException.isProxyError());
    }

}