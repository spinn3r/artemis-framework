package com.spinn3r.artemis.network;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CookieDecoderTest {

    @Test
    public void testDecode() throws Exception {

        Cookie cookie = CookieDecoder.decode( "YSC=fNc9hMYIAEo; path=/; domain=.youtube.com; httponly" );

        assertNotNull( cookie );
        assertEquals( "Cookie{name='YSC', value='fNc9hMYIAEo', path='/', domain='.youtube.com', httpOnly=true}", cookie.toString() );

    }

    @Test
    public void testDecode1() throws Exception {

        Cookie cookie = CookieDecoder.decode( "NAME=VALUE; expires=DATE; path=PATH; domain=DOMAIN_NAME; secure" );
        assertNotNull( cookie );
        assertEquals( "Cookie{name='NAME', value='VALUE', path='PATH', domain='DOMAIN_NAME', httpOnly=false}", cookie.toString() );

    }

}