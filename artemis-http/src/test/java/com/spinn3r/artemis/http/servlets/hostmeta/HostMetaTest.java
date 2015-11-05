package com.spinn3r.artemis.http.servlets.hostmeta;

import com.spinn3r.artemis.init.ServiceReferences;
import org.junit.Test;

import static org.junit.Assert.*;

public class HostMetaTest {

    @Test
    public void testToJSON() throws Exception {

        HostMeta hostMeta = new HostMeta( "1.0.0", "api", "localhost", "0m30s" );

        assertEquals( "{\n" +
                        "  \"version\" : \"1.0.0\",\n" +
                        "  \"role\" : \"api\",\n" +
                        "  \"hostname\" : \"localhost\",\n" +
                        "  \"uptime\" : \"0m30s\"\n" +
                        "}", hostMeta.toJSON() );

    }

}