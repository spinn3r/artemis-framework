package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.config.TestResourcesConfigLoader;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class DefaultDirectHttpRequestBuilderTest extends BaseLauncherTest {

    @Inject
    DirectHttpRequestBuilder directHttpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {

        TestResourcesConfigLoader configLoader = new TestResourcesConfigLoader( "src/test/resources/profiles/noproxy" );

        super.setUp( configLoader, DirectNetworkService.class );
    }

    @Test
    public void testTwitterSettings() throws Exception {

        HttpRequestMethod httpRequestMethod = directHttpRequestBuilder.get( "http://twitter.com" );

        httpRequestMethod.execute();

        assertFalse( httpRequestMethod.getFollowContentRedirects() );

    }

}