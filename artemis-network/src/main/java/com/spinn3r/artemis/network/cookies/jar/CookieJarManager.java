package com.spinn3r.artemis.network.cookies.jar;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows us to map URLs to cookie jars that support cookies for this URL.
 */
public class CookieJarManager {

    private final List<CookieJarReference> cookieJarReferences;

    private final List<CookieJarHolder> cookieJarHolders = Lists.newArrayList();

    public CookieJarManager(List<CookieJarReference> cookieJarReferences) throws IOException {
        this.cookieJarReferences = cookieJarReferences;

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
        File file = new File(cookieJarReference.getPath());
        CookieJar cookieJar = new FileBackedCookieJar(file);
        Pattern pattern = Pattern.compile(cookieJarReference.getRegex());

        return new CookieJarHolder(cookieJarReference, pattern, cookieJar);
    }

}
