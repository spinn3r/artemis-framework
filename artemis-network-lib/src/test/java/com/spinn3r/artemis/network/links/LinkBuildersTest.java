package com.spinn3r.artemis.network.links;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LinkBuildersTest {

    @Test
    public void testParse() throws Exception {

        List<String> schemes = Lists.newArrayList( "http", "https" );
        List<String> hosts = Lists.newArrayList( "cnn.com", "127.0.0.1" );
        List<Integer> ports = Lists.newArrayList( -1, 80, 443, 9090, 12345 );
        List<String> paths = Lists.newArrayList( "", "/", "/foo", "/foo/bar" );

        List<String> queryDatas = Lists.newArrayList( "", "?", "?cat=dog", "?cat=dog&foo=bar", "?foo" );

        for (String scheme : schemes) {
            for (String host : hosts) {
                for (int port : ports) {
                    for (String path : paths) {

                        for (String queryData : queryDatas) {

                            String link = String.format( "%s://%s", scheme, host );

                            if ( port > 0 ) {
                                link += ":" + port;
                            }


                            link += path;

                            link += queryData;

                            test( link );

                        }

                    }
                }
            }
        }

//        test( "http://cnn.com" );
//        test( "http://cnn.com/foo" );
//        test( "http://cnn.com/foo/index.html" );
//
//        test( "http://cnn.com:8080" );
//        test( "http://cnn.com:8080/foo" );
//        test( "http://cnn.com:8080/foo/index.html" );
//
//        test( "http://cnn.com:80" );
//        test( "http://cnn.com:80/foo" );
//        test( "http://cnn.com:80/foo/index.html" );

    }

    private void test( String link ) {

        System.out.printf( "%s\n", link );

        LinkBuilder linkBuilder = LinkBuilders.parse( link );

        String formatted = linkBuilder.format();

        String expected = link;

        if ( expected.contains( ":80" ) ) {
            expected = expected.replace( ":80", "" );
        }

        if ( expected.endsWith( "?" ) ) {
            expected = expected.substring( 0, expected.length() -1 );
        }

        assertEquals( expected, formatted );

    }

}