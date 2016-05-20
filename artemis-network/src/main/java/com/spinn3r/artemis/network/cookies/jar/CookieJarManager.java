package com.spinn3r.artemis.network.cookies.jar;

import com.google.common.collect.Lists;
import com.spinn3r.log5j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows us to map URLs to cookie jars that support cookies for this URL.
 */
public class CookieJarManager {

    private static final Logger log = Logger.getLogger();

    private final List<CookieJarHolder> cookieJarHolders = Lists.newArrayList();

    public CookieJarManager(List<CookieJarReference> cookieJarReferences) throws IOException {

        for (CookieJarReference cookieJarReference : cookieJarReferences) {
            cookieJarHolders.add(createCookieJarHolder(cookieJarReference));
        }

    }

    public CookieJar getCookieJar(String link ) {

        for (CookieJarHolder cookieJarHolder : cookieJarHolders) {
            Matcher matcher = cookieJarHolder.getPattern().matcher(link);
            if (matcher.find()) {
                return cookieJarHolder.getCookieJar();
            }
        }

        return new EmptyCookieJar();

    }

    private CookieJarHolder createCookieJarHolder(CookieJarReference cookieJarReference) throws IOException {

        Pattern pattern = Pattern.compile(cookieJarReference.getRegex());

        if (cookieJarReference.getPath() != null) {

            log.info( "Loading cookie jar from file: %s", cookieJarReference.getPath());

            File file = new File(cookieJarReference.getPath());
            CookieJar cookieJar = new FileBackedCookieJar(file);

            return new CookieJarHolder(cookieJarReference, pattern, cookieJar);

        } else if ( cookieJarReference.getStore() != null ) {

            CookieJar cookieJar = new BackedCookieJar(cookieJarReference.getStore());
            return new CookieJarHolder(cookieJarReference, pattern, cookieJar);

        } else {
            throw new RuntimeException("Neither path nor store provided");
        }

    }

}
