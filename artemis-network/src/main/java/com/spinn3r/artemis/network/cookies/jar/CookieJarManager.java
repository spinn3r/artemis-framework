package com.spinn3r.artemis.network.cookies.jar;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.log5j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows us to map URLs to cookie jars that support cookies for this URL.
 */
public class CookieJarManager {

    private static final Logger log = Logger.getLogger();

    private final ConfigLoader configLoader;

    private final List<CookieJarHolder> cookieJarHolders = Lists.newArrayList();

    CookieJarManager(ConfigLoader configLoader, List<CookieJarReference> cookieJarReferences) throws IOException {
        this.configLoader = configLoader;

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

            log.info("Loading cookie jar from file: %s ...", cookieJarReference.getPath());

            File file = new File(cookieJarReference.getPath());
            CookieJar cookieJar = new FileBackedCookieJar(file, cookieJarReference.getFormat());

            log.info("Loading cookie jar from file: %s ... done (loaded %,d cookies)", cookieJarReference.getConfigPath(), cookieJar.size());

            return new CookieJarHolder(cookieJarReference, pattern, cookieJar);

        } else if ( cookieJarReference.getConfigPath() != null ) {

            log.info("Loading cookie jar from config path: %s ...", cookieJarReference.getConfigPath());

            InputStream inputStream = configLoader.getResource(getClass(), cookieJarReference.getConfigPath()).openStream();
            CookieJar cookieJar = new FileBackedCookieJar(inputStream, cookieJarReference.getFormat());

            log.info("Loading cookie jar from config path: %s ... done (loaded %,d cookies)", cookieJarReference.getConfigPath(), cookieJar.size());

            return new CookieJarHolder(cookieJarReference, pattern, cookieJar);

        } else if ( cookieJarReference.getStore() != null ) {

            CookieJar cookieJar = new BackedCookieJar(cookieJarReference.getStore());
            return new CookieJarHolder(cookieJarReference, pattern, cookieJar);

        } else {
            throw new RuntimeException("Neither path nor store provided");
        }

    }

}
