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

        assertEquals( "Cookie{name='csrftoken', value='201a762b9af5f6d96f08edbf7342263a', version='VERSION_1_RFC2965', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}",
                      cookies.get( "csrftoken" ).toString() );

        assertEquals( "Cookie{name='target_sig', value='', version='VERSION_1_RFC2965', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}",
                      cookies.get( "target_sig" ).toString() );

        assertEquals( "Cookie{name='target', value='', version='VERSION_1_RFC2965', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}",
                      cookies.get( "target" ).toString() );

        assertEquals( "Cookie{name='mid', value='VnBmeQAEAAHoKkHWDef1Ex8tvA2w', version='VERSION_1_RFC2965', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}",
                      cookies.get( "mid" ).toString() );

    }

    @Test
    public void testToMap() throws Exception {

        ImmutableMap<String, String> cookies = Cookies.toMap("guest_id=v1%3A148460110141630082; lang=en; moments_profile_moments_nav_tooltip_self=true; kdt=azDe92DEp7fBaRQLdHpbzkwSO1p3Y98nNRCnB5eV; remember_checked_on=1; twid=\"u=794670871076020224\"; auth_token=77d57eacd3e2157d7a336f627ec8c69636e0cb7f; pid=\"v3:1484685570836211414620834\"; _ga=GA1.2.959178117.1484601103; _gat=1; _twitter_sess=BAh7CiIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCGoMH6lZAToMY3NyZl9p%250AZCIlZDA2N2VhNzQ2YmQ0NmY2NzMzZTVkODNkYjgwZTAyY2I6B2lkIiUwNTQ1%250ANGQ5NGU5YTk1MDZmZGVhZTJiNjJhOTFiZjc1NzoJdXNlcmwrCQAg1mfyPAcL--d5b21e6f38566a346f7f1c116d8360e5c55defaa");

        assertEquals(11, cookies.size());

    }

}