package com.spinn3r.artemis.network;

import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.cookies.Cookie;
import com.spinn3r.artemis.network.cookies.CookieDecoder;
import com.spinn3r.artemis.util.text.MapFormatter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import static org.junit.Assert.*;

public class CookieDecoderTest {

    @Test
    public void testDecodeSingleResponseCookie() throws Exception {

        Cookie cookie = CookieDecoder.decodeSingleResponseCookie("YSC=fNc9hMYIAEo; path=/; domain=.youtube.com; httponly" );

        assertNotNull( cookie );
        assertEquals( "Cookie{name='YSC', value='fNc9hMYIAEo', version='VERSION_1_RFC2965', path=Optional[/], domain=Optional[.youtube.com], httpOnly=true, secure=false, maxAge=Optional.empty}",
                      cookie.toString() );

    }

    @Test
    public void testDecodeSingleResponseCookie1() throws Exception {

        Cookie cookie = CookieDecoder.decodeSingleResponseCookie("foo=bar; expires=DATE; path=.; domain=example.com; secure" );
        assertNotNull( cookie );

        assertEquals( "Cookie{name='foo', value='bar', version='VERSION_1_RFC2965', path=Optional[.], domain=Optional[example.com], httpOnly=false, secure=true, maxAge=Optional.empty}",
                      cookie.toString() );

    }

    @Test
    public void testDecodeRequestCookies() throws Exception {

        String header = "guest_id=v1%3A147829923045826858; pid=\"v3:1478299232355003518379823\"; kdt=KlWkmWFS5F3rcASjPYqBYQx2QtuK2YrlNmQvOHvT; remember_checked_on=1; twid=\"u=794670871076020224\"; auth_token=29b2790263812eaf61758f3763089fcde8182987; lang=en; external_referer=\"ha6p0n1EwrrghvLstouCFIWH5bCiWdsOm9ONXtytu/pIPxDcz96DON4hDm01i1K2|d1\"; _ga=GA1.2.766550189.1478299232; _twitter_sess=BAh7DCIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCE8hgDFYAToMY3NyZl9p%250AZCIlNGZkMDA3YmZlYWYzNjI2Yzg2NzYwZTE5ZGM0M2I3ZDE6B2lkIiU3MmIx%250ANGMxY2JkOGFlNjdiZjNmZGI0ZWNkYThjNTY1MDoJdXNlcmwrCQAg1mfyPAcL%250AOg51c2VyX2luZm97ADoNbnV4X2Zsb3ciCG51eA%253D%253D--ec4804eaae8385ae35fe0711eaeddd449995e5e0";

        ImmutableMap<String, String> cookies = CookieDecoder.decodeRequestCookies(header);

        assertEquals("guest_id: v1%3A147829923045826858\n" +
                       "pid: \"v3:1478299232355003518379823\"\n" +
                       "kdt: KlWkmWFS5F3rcASjPYqBYQx2QtuK2YrlNmQvOHvT\n" +
                       "remember_checked_on: 1\n" +
                       "twid: \"u\n" +
                       "auth_token: 29b2790263812eaf61758f3763089fcde8182987\n" +
                       "lang: en\n" +
                       "external_referer: \"ha6p0n1EwrrghvLstouCFIWH5bCiWdsOm9ONXtytu/pIPxDcz96DON4hDm01i1K2|d1\"\n" +
                       "_ga: GA1.2.766550189.1478299232\n" +
                       "_twitter_sess: BAh7DCIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCE8hgDFYAToMY3NyZl9p%250AZCIlNGZkMDA3YmZlYWYzNjI2Yzg2NzYwZTE5ZGM0M2I3ZDE6B2lkIiU3MmIx%250ANGMxY2JkOGFlNjdiZjNmZGI0ZWNkYThjNTY1MDoJdXNlcmwrCQAg1mfyPAcL%250AOg51c2VyX2luZm97ADoNbnV4X2Zsb3ciCG51eA%253D%253D--ec4804eaae8385ae35fe0711eaeddd449995e5e0\n",
                     MapFormatter.table(cookies));

    }

    @Test
    public void testParseNameValuePairWithEmptyValue() throws Exception {

        ImmutablePair<String, String> pair = CookieDecoder.nameValuePair("foo=");

        assertNotNull( pair );
        assertEquals( "(foo,)", pair.toString() );

    }
}