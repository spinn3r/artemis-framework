package com.spinn3r.artemis.network.query;

import java.awt.DisplayMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.PKIXRevocationChecker;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A URI query for a fluent API on top of java.net.URI.
 */
public class UriQuery {

    private static final Pattern DOMAIN_PATTERN = Pattern.compile("[^.]+\\.[^.]+$");

    private URI uri;

    private UriQuery( String url ) {

        try {

            this.uri = new URI( url );

        } catch (URISyntaxException e) {
            throw new RuntimeException( e );
        }

    }

    public String host() {
        return uri.getHost();
    }

    public Optional<String> domain() {

        String host = host();

        if (host == null)
            return Optional.empty();

        Matcher matcher = DOMAIN_PATTERN.matcher(host);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        }

        return Optional.empty();

    }

    public static UriQuery uri( String value ) {
        return new UriQuery( value );
    }

}

