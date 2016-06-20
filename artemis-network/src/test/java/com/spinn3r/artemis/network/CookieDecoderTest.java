package com.spinn3r.artemis.network;

import com.spinn3r.artemis.network.cookies.Cookie;
import com.spinn3r.artemis.network.cookies.CookieDecoder;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CookieDecoderTest {

    @Test
    public void testDecode() throws Exception {

        Cookie cookie = CookieDecoder.decode("YSC=fNc9hMYIAEo; path=/; domain=.youtube.com; httponly" );

        assertNotNull( cookie );
        assertEquals( "Cookie{name='YSC', value='fNc9hMYIAEo', path=Optional[/], domain=Optional[.youtube.com], httpOnly=true, secure=false, maxAge=null}",
                      cookie.toString() );

    }

    @Test
    public void testDecode1() throws Exception {

        Cookie cookie = CookieDecoder.decode("foo=bar; expires=DATE; path=.; domain=example.com; secure" );
        assertNotNull( cookie );

        assertEquals( "Cookie{name='foo', value='bar', path=Optional[.], domain=Optional[example.com], httpOnly=false, secure=true, maxAge=null}",
                      cookie.toString() );

    }

    @Test
    public void testParseNameValuePairWithEmptyValue() throws Exception {

        CookieDecoder.NVP nameValuePair = CookieDecoder.nameValuePair( "foo=" );

        assertNotNull( nameValuePair );
        assertEquals( "NVP{name='foo', value=''}", nameValuePair.toString() );

    }
}