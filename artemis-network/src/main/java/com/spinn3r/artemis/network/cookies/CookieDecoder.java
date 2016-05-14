package com.spinn3r.artemis.network.cookies;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class CookieDecoder {

    private static final Pattern NAME_VALUE_PATTERN = Pattern.compile( "([^=]+)=([^=]*)" );

    public static Cookie decode( String data ) {

        String[] parts = data.split( "; " );

        if ( parts.length < 1 ) {
            return null;
        }

        NVP main = nameValuePair( parts[0] );

        if ( main == null ) {
            return null;
        }

        String name = main.getName();
        String value = main.getValue();
        String path = null;
        String domain = null;
        boolean httpOnly = false;
        boolean secure = false;

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];

            if ( "httponly".equals( part ) ) {
                httpOnly = true;
                continue;
            }

            if ( "secure".equals( part ) ) {
                secure = true;
                continue;
            }

            if ( part.contains( "=" ) ) {

                NVP nvp = nameValuePair( part );

                if ( nvp == null )
                    continue;

                switch ( nvp.getName().toLowerCase() ) {

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

        return new Cookie( name, value, path, domain , httpOnly );

    }

    protected static NVP nameValuePair( String value ) {

        Matcher matcher = NAME_VALUE_PATTERN.matcher( value );

        if ( matcher.find() ) {
            return new NVP( matcher.group(1), matcher.group(2) );
        } else {
            return null;
        }

    }

    static class NVP {

        private final String name;

        private final String value;

        public NVP(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "NVP{" +
                     "name='" + name + '\'' +
                     ", value='" + value + '\'' +
                     '}';
        }
    }

}
