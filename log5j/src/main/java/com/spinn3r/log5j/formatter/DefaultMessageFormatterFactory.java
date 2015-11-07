package com.spinn3r.log5j.formatter;

import java.util.Locale;

/**
 * User: Denis Tarima
 * Date: Jun 4, 2010
 */
public class DefaultMessageFormatterFactory implements MessageFormatterFactory {

    private static final String __prefix = "log5j.formatter.default.";

    private final Locale _locale;

    public DefaultMessageFormatterFactory() {
        String language = System.getProperty(__prefix + "language", null);
        String country = System.getProperty(__prefix + "country", null);
        String variant = System.getProperty(__prefix + "variant", null);

        if (variant != null) {
            _locale = new Locale(language, country, variant);
        } else if (country != null) {
            _locale = new Locale(language, country);
        } else if (language != null) {
            _locale = new Locale(language);
        } else {
            _locale = Locale.getDefault();
        }
    }

    public MessageFormatter create() {
        return new DefaultMessageFormatter(_locale);
    }
}