package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
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
        super.setUp( DirectNetworkService.class );
    }

    @Test
    public void testTwitterSettings() throws Exception {

        HttpRequestMethod httpRequestMethod = directHttpRequestBuilder.get( "http://twitter.com" );

        httpRequestMethod.execute();

        assertFalse( httpRequestMethod.getFollowContentRedirects() );

//        if ( getResource().matches( "https?://twitter\\.com(/.*)?" ) ) {
//            delegate.withFollowContentRedirects( false );
//
//
//        }

    }

}