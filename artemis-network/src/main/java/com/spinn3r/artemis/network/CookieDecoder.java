package com.spinn3r.artemis.network;

/**
 *
 */
public class CookieDecoder {

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

    private static NVP nameValuePair( String value ) {

        String[] split = value.split( "=" );

        if ( split.length != 2 ) {
            return null;
        }

        return new NVP( split[0], split[1] );

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

    }

}
