package com.spinn3r.artemis.network.cookies;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class CookieDecoder {

    private static final Pattern NAME_VALUE_PATTERN = Pattern.compile( "([^=]+)=([^=]*)" );

    /**
     * Decode a response cookie like:
     *
     * <code>
     *     twitter_sess=BAh7DCIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCE8hgDFYAToMY3NyZl9p%250AZCIlNGZkMDA3YmZlYWYzNjI2Yzg2NzYwZTE5ZGM0M2I3ZDE6B2lkIiU3MmIx%250ANGMxY2JkOGFlNjdiZjNmZGI0ZWNkYThjNTY1MDoJdXNlcmwrCQAg1mfyPAcL%250AOg51c2VyX2luZm97ADoNbnV4X2Zsb3ciCG51eA%253D%253D--ec4804eaae8385ae35fe0711eaeddd449995e5e0; Path=/; Domain=.twitter.com; Secure; HTTPOnly
     * </code>
     *
     * @param data
     * @return
     */
    public static Cookie decodeSingleResponseCookie(String data ) {

        String[] parts = data.split( "; " );

        if ( parts.length < 1 ) {
            return null;
        }

        ImmutablePair<String, String> pair = nameValuePair(parts[ 0 ]);

        if ( pair == null ) {
            return null;
        }

        String name = pair.getLeft();
        String value = pair.getRight();
        String path = null;
        String domain = null;
        boolean httpOnly = false;
        boolean secure = false;

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];

            if ( "httponly".equalsIgnoreCase( part ) ) {
                httpOnly = true;
                continue;
            }

            if ( "secure".equalsIgnoreCase( part ) ) {
                secure = true;
                continue;
            }

            if ( part.contains( "=" ) ) {

                ImmutablePair<String, String> nvp = nameValuePair( part );

                if ( nvp == null )
                    continue;

                switch ( nvp.getLeft().toLowerCase() ) {

                    case "path":
                        path = nvp.getValue();
                        break;

                    case "domain":
                        domain = nvp.getValue();
                        break;

                    default:
                        break;

                }

            }

        }

        return new Cookie.Builder(name,value)
            .setPath( Optional.ofNullable(path) )
            .setDomain(Optional.ofNullable(domain))
            .setHttpOnly(httpOnly)
            .setSecure(secure)
            .build();

    }

    /**
     * Decode a raw HTTP header Cookie value and break the result into name value pairs.
     *
     * We don't have the real cookies so we can only return pairs.
     *
     * Example:
     *
     * <code>
     *     guest_id=v1%3A147829923045826858; pid="v3:1478299232355003518379823"; kdt=KlWkmWFS5F3rcASjPYqBYQx2QtuK2YrlNmQvOHvT; remember_checked_on=1; twid="u=794670871076020224"; auth_token=29b2790263812eaf61758f3763089fcde8182987; lang=en; external_referer="ha6p0n1EwrrghvLstouCFIWH5bCiWdsOm9ONXtytu/pIPxDcz96DON4hDm01i1K2|1"; _ga=GA1.2.766550189.1478299232; _twitter_sess=BAh7DCIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCE8hgDFYAToMY3NyZl9p%250AZCIlNGZkMDA3YmZlYWYzNjI2Yzg2NzYwZTE5ZGM0M2I3ZDE6B2lkIiU3MmIx%250ANGMxY2JkOGFlNjdiZjNmZGI0ZWNkYThjNTY1MDoJdXNlcmwrCQAg1mfyPAcL%250AOg51c2VyX2luZm97ADoNbnV4X2Zsb3ciCG51eA%253D%253D--ec4804eaae8385ae35fe0711eaeddd449995e5e0
     * </code>
     *
     */
    public static ImmutableMap<String,String> decodeRequestCookies(String header) {

        Map<String,String> result = Maps.newLinkedHashMap();

        String[] parts = header.split( "; " );

        for (String part : parts) {
            String[] nameAndValue = part.split("=");

            result.put(nameAndValue[0], nameAndValue[1]);

        }

        return ImmutableMap.copyOf(result);

    }

    public static ImmutablePair<String,String> nameValuePair(String value ) {

        Matcher matcher = NAME_VALUE_PATTERN.matcher( value );

        if ( matcher.find() ) {
            return new ImmutablePair<>( matcher.group(1), matcher.group(2) );
        } else {
            return null;
        }

    }

}
