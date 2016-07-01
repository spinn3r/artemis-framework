package com.spinn3r.artemis.network.links;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Strings;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 */
public class Links {

    private static final UrlValidator URL_VALIDATOR = new UrlValidator(Strings.toArray(Lists.newArrayList("http", "https")),
                                                                       UrlValidator.ALLOW_LOCAL_URLS + UrlValidator.ALLOW_2_SLASHES);

    /**
     * Return true if this is a valid HTTP URL which we can parse.  It still might
     * not be fetchable (perhaps because the URL is down) but at least its valid.
     */
    public static boolean isValid(String link) {

        if ( link == null )
            return false;

        // try to fail fast
        if (! link.startsWith("http:") && ! link.startsWith("https:")) {
            return false;
        }

        // try to be as liberal as possible.
        return isValidWithValidator(link) || isValidAsURI(link);

    }

    // parse via the Apache commons URL validator

    private static boolean isValidWithValidator(String link) {
        return URL_VALIDATOR.isValid(link);
    }

    // parse using the JDK URI component which accepts unicode path names
    private static boolean isValidAsURI(String link) {

        try {
            new URI(link);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }

    }

    public static void checkValid(String link) {

        if ( ! isValid(link)) {
            throw new IllegalArgumentException( String.format( "Link is not valid: '%s'", link ) );
        }
    }


    public static Link parse( String link ) {

        try {

            // http://stackoverflow.com/questions/13592236/parse-the-uri-string-into-name-value-collection-in-java

            URI uri = new URI( link );

            List<NameValuePair> nameValuePairs = URLEncodedUtils.parse( uri, "UTF-8" );

            LinkedHashMap<String,String> params = new LinkedHashMap<>();

            for (NameValuePair nameValuePair : nameValuePairs) {
                params.put( nameValuePair.getName(), nameValuePair.getValue() );
            }

            return new Link( uri, params );

        } catch (URISyntaxException e) {
            throw new RuntimeException( "Could not parse URL: ", e );
        }

    }

}
