package com.spinn3r.artemis.network.links;

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
