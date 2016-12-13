package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.LauncherTest;
import com.spinn3r.artemis.init.config.MultiConfigLoaders;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.init.config.TestResourcesConfigLoader;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultDirectHttpRequestBuilderTest extends LauncherTest {

    @Inject
    DirectHttpRequestBuilder directHttpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {

        setConfigLoader(MultiConfigLoaders.create(new TestResourcesConfigLoader( "src/test/resources/profiles/noproxy" ),
                                                  new ResourceConfigLoader()));

        setServiceReferences(ImmutableList.of(DirectNetworkService.class));

        super.setUp();

    }

    @Test
    public void testTwitterSettings() throws Exception {

        HttpRequestMethod httpRequestMethod = directHttpRequestBuilder.get( "http://twitter.com" );

        httpRequestMethod.execute();

        assertFalse( httpRequestMethod.getFollowContentRedirects() );

    }

}