package com.spinn3r.artemis.network;

import com.spinn3r.artemis.network.builder.HttpRequestMethods;
import org.junit.Test;

import static org.junit.Assert.*;

public class MockHttpRequestBuilderTest {


    @Test
    public void test1() throws Exception {

        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();

        String resource = "http://cnn.com";

        MockHttpRequestMethod mockHttpRequestMethod = new MockHttpRequestMethod( resource );

        mockHttpRequestMethod.setContentWithEncoding( "hello world" );
        mockHttpRequestMethod.setResponseCode( 200 );

        mockHttpRequestBuilder.register( HttpRequestMethods.GET, mockHttpRequestMethod );

        assertEquals( "hello world", mockHttpRequestBuilder.get( resource ).execute().getContentWithEncoding() );

    }

}