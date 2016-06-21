package com.spinn3r.artemis.network;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.network.cookies.Cookie;
import com.spinn3r.artemis.network.cookies.Cookies;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CookiesTest {

    @Test
    public void testInstagram1() throws Exception {

        // test parsing cookies sent by instagram.

        List<String> setCookies =
          Lists.newArrayList(
            "csrftoken=201a762b9af5f6d96f08edbf7342263a; expires=Tue, 13-Dec-2016 19:14:01 GMT; Max-Age=31449600; Path=/",
            "target_sig=; expires=Thu, 01-Jan-1970 00:00:00 GMT; Max-Age=0; Path=/",
            "target=; expires=Thu, 01-Jan-1970 00:00:00 GMT; Max-Age=0; Path=/",
            "mid=VnBmeQAEAAHoKkHWDef1Ex8tvA2w; expires=Mon, 10-Dec-2035 19:14:01 GMT; Max-Age=630720000; Path=/" );

        ImmutableMap<String, Cookie> cookies = Cookies.fromSetCookiesList(setCookies );

        assertEquals( "Cookie{name='csrftoken', value='201a762b9af5f6d96f08edbf7342263a', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}",
                      cookies.get( "csrftoken" ).toString() );

        assertEquals( "Cookie{name='target_sig', value='', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}",
                      cookies.get( "target_sig" ).toString() );

        assertEquals( "Cookie{name='target', value='', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}",
                      cookies.get( "target" ).toString() );

        assertEquals( "Cookie{name='mid', value='VnBmeQAEAAHoKkHWDef1Ex8tvA2w', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}",
                      cookies.get( "mid" ).toString() );

    }

}