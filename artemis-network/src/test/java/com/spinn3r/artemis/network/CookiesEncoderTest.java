package com.spinn3r.artemis.network;

import com.spinn3r.artemis.network.cookies.CookiesEncoder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CookiesEncoderTest {

    @Test
    public void testEncodeWithoutCookies() throws Exception {

        Map<String,String> cookies = new TreeMap<>();

        assertEquals("",
                     CookiesEncoder.encode(cookies ) );

    }

    @Test
    public void testEncodeWithOneCookie() throws Exception {

        Map<String,String> cookies = new TreeMap<>();

        cookies.put( "hello", "world" );

        assertEquals( "hello=world",
                      CookiesEncoder.encode( cookies ) );

    }

    @Test
    public void testEncodeWithTwoCookies() throws Exception {

        Map<String,String> cookies = new TreeMap<>();

        cookies.put( "hello", "world" );
        cookies.put( "cat", "dog" );

        assertEquals( "cat=dog; hello=world",
                      CookiesEncoder.encode( cookies ) );

    }


}