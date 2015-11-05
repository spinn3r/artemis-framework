package com.spinn3r.artemis.network;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Given a string of HTML content we decode the entities it contains.
 */
public class EntityDecoder {

    public static Map<String,Character> LATIN1_ENTITIES = new HashMap<>();

    private static Map<String,Character> CDATA_ENTITIES  = new HashMap<>();

    private static Map<String,Character> ALL_ENTITIES    = new HashMap<>();

    static ThreadLocal pattern = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return Pattern.compile( "&([a-zA-Z0-9#]+);" );
        }
    };

    /**
     * Decode all LATIN1 entities into code points.
     */
    public static String decode( String content ) {
        return decode( content, LATIN1_ENTITIES );
    }

    /**
     * Decode CDATA entities (attribute data and HTML encoded data) such as:
     *
     * &gt;
     * &lt;
     *
     */
    public static String decodeCDATA( String content ) {
        return decode( content, CDATA_ENTITIES );
    }

    /**
     * Decode EVERYTHING including regular entities and CDATA entities.
     */
    public static String decodeAll( String content ) {
        return decode( content, ALL_ENTITIES );
    }

    /**
     * Decode content.  If a null is passed in we return null.
     *
     */
    public static String decode( String content,
                                 Map<String,Character> entities ) {

        if ( content == null )
            return null;

        StringBuilder buff = new StringBuilder( content.length() );

        Pattern p = (Pattern)pattern.get();
        Matcher m = p.matcher( content );

        int index = 0;

        while ( m.find() ) {

            //figure out which entity to escape or just include it.

            buff.append( content.substring( index, m.start( 0 ) ) );

            //NOTE: entities are required to be case insensitive be we want to
            //be a bit more open/flexible.
            String entity = m.group( 1 ).toLowerCase();

            //FIXME: cache all the matches patterns below.

            if ( entities.containsKey( entity ) ) {
                buff.append( entities.get( entity ) );
            } else if ( entity.startsWith( "#" ) ) {

                String value = entity.substring( 1, entity.length() );

                if ( value.matches( "x[0-9a-fA-F]+" ) ) {

                    //this is char literal

                    //trim the x prefix so that we can parse it as a hex string.
                    value = value.substring( 1, value.length() );
                    buff.append( (char)Integer.parseInt( value, 16 ) );

                } else if ( value.matches( "[0-9]+" ) ) {
                    //this is char literal
                    buff.append( (char)Integer.parseInt( value ) );

                } else {
                    buff.append( "?" );
                }

            } else {
                //found an entity we no NOTHING about.  Should we warn?
                buff.append( m.group( 0 ) );
            }

            index = m.end( 0 );

        }

        buff.append( content.substring( index, content.length() ) );

        return buff.toString();

    }

    static {

        // http://en.wikipedia.org/wiki/Character_encodings_in_HTML
        // http://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references

        CDATA_ENTITIES.put( "gt",         '>'       );
        CDATA_ENTITIES.put( "apos",       '\''      );
        CDATA_ENTITIES.put( "lt",         '<'       );
        CDATA_ENTITIES.put( "amp",        '&'       );
        CDATA_ENTITIES.put( "quot",       '\"'      );

        // Character entity references have the format &name; where "name" is a
        // case-sensitive alphanumeric string.

        LATIN1_ENTITIES.put( "nbsp",      (char)160 );
        LATIN1_ENTITIES.put( "iexcl",     (char)161 );
        LATIN1_ENTITIES.put( "cent",      (char)162 );
        LATIN1_ENTITIES.put( "pound",     (char)163 );
        LATIN1_ENTITIES.put( "curren",    (char)164 );
        LATIN1_ENTITIES.put( "yen",       (char)165 );
        LATIN1_ENTITIES.put( "brvbar",    (char)166 );
        LATIN1_ENTITIES.put( "sect",      (char)167 );
        LATIN1_ENTITIES.put( "uml",       (char)168 );
        LATIN1_ENTITIES.put( "copy",      (char)169 );
        LATIN1_ENTITIES.put( "ordf",      (char)170 );
        LATIN1_ENTITIES.put( "laquo",     (char)171 );
        LATIN1_ENTITIES.put( "not",       (char)172 );
        LATIN1_ENTITIES.put( "shy",       (char)173 );
        LATIN1_ENTITIES.put( "reg",       (char)174 );
        LATIN1_ENTITIES.put( "macr",      (char)175 );
        LATIN1_ENTITIES.put( "deg",       (char)176 );
        LATIN1_ENTITIES.put( "plusmn",    (char)177 );
        LATIN1_ENTITIES.put( "sup2",      (char)178 );
        LATIN1_ENTITIES.put( "sup3",      (char)179 );
        LATIN1_ENTITIES.put( "acute",     (char)180 );
        LATIN1_ENTITIES.put( "micro",     (char)181 );
        LATIN1_ENTITIES.put( "para",      (char)182 );
        LATIN1_ENTITIES.put( "middot",    (char)183 );
        LATIN1_ENTITIES.put( "cedil",     (char)184 );
        LATIN1_ENTITIES.put( "sup1",      (char)185 );
        LATIN1_ENTITIES.put( "ordm",      (char)186 );
        LATIN1_ENTITIES.put( "raquo",     (char)187 );
        LATIN1_ENTITIES.put( "frac14",    (char)188 );
        LATIN1_ENTITIES.put( "frac12",    (char)189 );
        LATIN1_ENTITIES.put( "frac34",    (char)190 );
        LATIN1_ENTITIES.put( "iquest",    (char)191 );
        LATIN1_ENTITIES.put( "agrave",    (char)192 );
        LATIN1_ENTITIES.put( "aacute",    (char)193 );
        LATIN1_ENTITIES.put( "acirc",     (char)194 );
        LATIN1_ENTITIES.put( "atilde",    (char)195 );
        LATIN1_ENTITIES.put( "auml",      (char)196 );
        LATIN1_ENTITIES.put( "aring",     (char)197 );
        LATIN1_ENTITIES.put( "aelig",     (char)198 );
        LATIN1_ENTITIES.put( "ccedil",    (char)199 );
        LATIN1_ENTITIES.put( "egrave",    (char)200 );
        LATIN1_ENTITIES.put( "eacute",    (char)201 );
        LATIN1_ENTITIES.put( "ecirc",     (char)202 );
        LATIN1_ENTITIES.put( "euml",      (char)203 );
        LATIN1_ENTITIES.put( "igrave",    (char)204 );
        LATIN1_ENTITIES.put( "iacute",    (char)205 );
        LATIN1_ENTITIES.put( "icirc",     (char)206 );
        LATIN1_ENTITIES.put( "iuml",      (char)207 );
        LATIN1_ENTITIES.put( "eth",       (char)208 );
        LATIN1_ENTITIES.put( "ntilde",    (char)209 );
        LATIN1_ENTITIES.put( "ograve",    (char)210 );
        LATIN1_ENTITIES.put( "oacute",    (char)211 );
        LATIN1_ENTITIES.put( "ocirc",     (char)212 );
        LATIN1_ENTITIES.put( "otilde",    (char)213 );
        LATIN1_ENTITIES.put( "ouml",      (char)214 );
        LATIN1_ENTITIES.put( "times",     (char)215 );
        LATIN1_ENTITIES.put( "oslash",    (char)216 );
        LATIN1_ENTITIES.put( "ugrave",    (char)217 );
        LATIN1_ENTITIES.put( "uacute",    (char)218 );
        LATIN1_ENTITIES.put( "ucirc",     (char)219 );
        LATIN1_ENTITIES.put( "uuml",      (char)220 );
        LATIN1_ENTITIES.put( "yacute",    (char)221 );
        LATIN1_ENTITIES.put( "thorn",     (char)222 );
        LATIN1_ENTITIES.put( "szlig",     (char)223 );
        LATIN1_ENTITIES.put( "agrave",    (char)224 );
        LATIN1_ENTITIES.put( "aacute",    (char)225 );
        LATIN1_ENTITIES.put( "acirc",     (char)226 );
        LATIN1_ENTITIES.put( "atilde",    (char)227 );
        LATIN1_ENTITIES.put( "auml",      (char)228 );
        LATIN1_ENTITIES.put( "aring",     (char)229 );
        LATIN1_ENTITIES.put( "aelig",     (char)230 );
        LATIN1_ENTITIES.put( "ccedil",    (char)231 );
        LATIN1_ENTITIES.put( "egrave",    (char)232 );
        LATIN1_ENTITIES.put( "eacute",    (char)233 );
        LATIN1_ENTITIES.put( "ecirc",     (char)234 );
        LATIN1_ENTITIES.put( "euml",      (char)235 );
        LATIN1_ENTITIES.put( "igrave",    (char)236 );
        LATIN1_ENTITIES.put( "iacute",    (char)237 );
        LATIN1_ENTITIES.put( "icirc",     (char)238 );
        LATIN1_ENTITIES.put( "iuml",      (char)239 );
        LATIN1_ENTITIES.put( "eth",       (char)240 );
        LATIN1_ENTITIES.put( "ntilde",    (char)241 );
        LATIN1_ENTITIES.put( "ograve",    (char)242 );
        LATIN1_ENTITIES.put( "oacute",    (char)243 );
        LATIN1_ENTITIES.put( "ocirc",     (char)244 );
        LATIN1_ENTITIES.put( "otilde",    (char)245 );
        LATIN1_ENTITIES.put( "ouml",      (char)246 );
        LATIN1_ENTITIES.put( "divide",    (char)247 );
        LATIN1_ENTITIES.put( "oslash",    (char)248 );
        LATIN1_ENTITIES.put( "ugrave",    (char)249 );
        LATIN1_ENTITIES.put( "uacute",    (char)250 );
        LATIN1_ENTITIES.put( "ucirc",     (char)251 );
        LATIN1_ENTITIES.put( "uuml",      (char)252 );
        LATIN1_ENTITIES.put( "yacute",    (char)253 );
        LATIN1_ENTITIES.put( "thorn",     (char)254 );
        LATIN1_ENTITIES.put( "yuml",      (char)255 );

        // HTML 4.0 entities

        LATIN1_ENTITIES.put( "oelig",     (char)338 );
        LATIN1_ENTITIES.put( "oelig",     (char)339 );
        LATIN1_ENTITIES.put( "scaron",    (char)352 );
        LATIN1_ENTITIES.put( "scaron",    (char)353 );
        LATIN1_ENTITIES.put( "yuml",      (char)376 );
        LATIN1_ENTITIES.put( "fnof",      (char)402 );
        LATIN1_ENTITIES.put( "circ",      (char)710 );
        LATIN1_ENTITIES.put( "tilde",     (char)732 );
        LATIN1_ENTITIES.put( "alpha",     (char)913 );
        LATIN1_ENTITIES.put( "beta",      (char)914 );
        LATIN1_ENTITIES.put( "gamma",     (char)915 );
        LATIN1_ENTITIES.put( "delta",     (char)916 );
        LATIN1_ENTITIES.put( "epsilon",   (char)917 );
        LATIN1_ENTITIES.put( "zeta",      (char)918 );
        LATIN1_ENTITIES.put( "eta",       (char)919 );
        LATIN1_ENTITIES.put( "theta",     (char)920 );
        LATIN1_ENTITIES.put( "iota",      (char)921 );
        LATIN1_ENTITIES.put( "kappa",     (char)922 );
        LATIN1_ENTITIES.put( "lambda",    (char)923 );
        LATIN1_ENTITIES.put( "mu",        (char)924 );
        LATIN1_ENTITIES.put( "nu",        (char)925 );
        LATIN1_ENTITIES.put( "xi",        (char)926 );
        LATIN1_ENTITIES.put( "omicron",   (char)927 );
        LATIN1_ENTITIES.put( "pi",        (char)928 );
        LATIN1_ENTITIES.put( "rho",       (char)929 );
        LATIN1_ENTITIES.put( "sigma",     (char)931 );
        LATIN1_ENTITIES.put( "tau",       (char)932 );
        LATIN1_ENTITIES.put( "upsilon",   (char)933 );
        LATIN1_ENTITIES.put( "phi",       (char)934 );
        LATIN1_ENTITIES.put( "chi",       (char)935 );
        LATIN1_ENTITIES.put( "psi",       (char)936 );
        LATIN1_ENTITIES.put( "omega",     (char)937 );
        LATIN1_ENTITIES.put( "alpha",     (char)945 );
        LATIN1_ENTITIES.put( "beta",      (char)946 );
        LATIN1_ENTITIES.put( "gamma",     (char)947 );
        LATIN1_ENTITIES.put( "delta",     (char)948 );
        LATIN1_ENTITIES.put( "epsilon",   (char)949 );
        LATIN1_ENTITIES.put( "zeta",      (char)950 );
        LATIN1_ENTITIES.put( "eta",       (char)951 );
        LATIN1_ENTITIES.put( "theta",     (char)952 );
        LATIN1_ENTITIES.put( "iota",      (char)953 );
        LATIN1_ENTITIES.put( "kappa",     (char)954 );
        LATIN1_ENTITIES.put( "lambda",    (char)955 );
        LATIN1_ENTITIES.put( "mu",        (char)956 );
        LATIN1_ENTITIES.put( "nu",        (char)957 );
        LATIN1_ENTITIES.put( "xi",        (char)958 );
        LATIN1_ENTITIES.put( "omicron",   (char)959 );
        LATIN1_ENTITIES.put( "pi",        (char)960 );
        LATIN1_ENTITIES.put( "rho",       (char)961 );
        LATIN1_ENTITIES.put( "sigmaf",    (char)962 );
        LATIN1_ENTITIES.put( "sigma",     (char)963 );
        LATIN1_ENTITIES.put( "tau",       (char)964 );
        LATIN1_ENTITIES.put( "upsilon",   (char)965 );
        LATIN1_ENTITIES.put( "phi",       (char)966 );
        LATIN1_ENTITIES.put( "chi",       (char)967 );
        LATIN1_ENTITIES.put( "psi",       (char)968 );
        LATIN1_ENTITIES.put( "omega",     (char)969 );
        LATIN1_ENTITIES.put( "thetasym",  (char)977 );
        LATIN1_ENTITIES.put( "upsih",     (char)978 );
        LATIN1_ENTITIES.put( "piv",       (char)982 );
        LATIN1_ENTITIES.put( "ensp",      (char)8194 );
        LATIN1_ENTITIES.put( "emsp",      (char)8195 );
        LATIN1_ENTITIES.put( "thinsp",    (char)8201 );
        LATIN1_ENTITIES.put( "zwnj",      (char)8204 );
        LATIN1_ENTITIES.put( "zwj",       (char)8205 );
        LATIN1_ENTITIES.put( "lrm",       (char)8206 );
        LATIN1_ENTITIES.put( "rlm",       (char)8207 );
        LATIN1_ENTITIES.put( "ndash",     (char)8211 );
        LATIN1_ENTITIES.put( "mdash",     (char)8212 );
        LATIN1_ENTITIES.put( "lsquo",     (char)8216 );
        LATIN1_ENTITIES.put( "rsquo",     (char)8217 );
        LATIN1_ENTITIES.put( "sbquo",     (char)8218 );
        LATIN1_ENTITIES.put( "ldquo",     (char)8220 );
        LATIN1_ENTITIES.put( "rdquo",     (char)8221 );
        LATIN1_ENTITIES.put( "bdquo",     (char)8222 );
        LATIN1_ENTITIES.put( "dagger",    (char)8224 );
        LATIN1_ENTITIES.put( "dagger",    (char)8225 );
        LATIN1_ENTITIES.put( "bull",      (char)8226 );
        LATIN1_ENTITIES.put( "hellip",    (char)8230 );
        LATIN1_ENTITIES.put( "permil",    (char)8240 );
        LATIN1_ENTITIES.put( "prime",     (char)8242 );
        LATIN1_ENTITIES.put( "prime",     (char)8243 );
        LATIN1_ENTITIES.put( "lsaquo",    (char)8249 );
        LATIN1_ENTITIES.put( "rsaquo",    (char)8250 );
        LATIN1_ENTITIES.put( "oline",     (char)8254 );
        LATIN1_ENTITIES.put( "frasl",     (char)8260 );
        LATIN1_ENTITIES.put( "euro",      (char)8364 );
        LATIN1_ENTITIES.put( "image",     (char)8465 );
        LATIN1_ENTITIES.put( "weierp",    (char)8472 );
        LATIN1_ENTITIES.put( "real",      (char)8476 );
        LATIN1_ENTITIES.put( "trade",     (char)8482 );
        LATIN1_ENTITIES.put( "alefsym",   (char)8501 );
        LATIN1_ENTITIES.put( "larr",      (char)8592 );
        LATIN1_ENTITIES.put( "uarr",      (char)8593 );
        LATIN1_ENTITIES.put( "rarr",      (char)8594 );
        LATIN1_ENTITIES.put( "darr",      (char)8595 );
        LATIN1_ENTITIES.put( "harr",      (char)8596 );
        LATIN1_ENTITIES.put( "crarr",     (char)8629 );
        LATIN1_ENTITIES.put( "larr",      (char)8656 );
        LATIN1_ENTITIES.put( "uarr",      (char)8657 );
        LATIN1_ENTITIES.put( "rarr",      (char)8658 );
        LATIN1_ENTITIES.put( "darr",      (char)8659 );
        LATIN1_ENTITIES.put( "harr",      (char)8660 );
        LATIN1_ENTITIES.put( "forall",    (char)8704 );
        LATIN1_ENTITIES.put( "part",      (char)8706 );
        LATIN1_ENTITIES.put( "exist",     (char)8707 );
        LATIN1_ENTITIES.put( "empty",     (char)8709 );
        LATIN1_ENTITIES.put( "nabla",     (char)8711 );
        LATIN1_ENTITIES.put( "isin",      (char)8712 );
        LATIN1_ENTITIES.put( "notin",     (char)8713 );
        LATIN1_ENTITIES.put( "ni",        (char)8715 );
        LATIN1_ENTITIES.put( "prod",      (char)8719 );
        LATIN1_ENTITIES.put( "sum",       (char)8721 );
        LATIN1_ENTITIES.put( "minus",     (char)8722 );
        LATIN1_ENTITIES.put( "lowast",    (char)8727 );
        LATIN1_ENTITIES.put( "radic",     (char)8730 );
        LATIN1_ENTITIES.put( "prop",      (char)8733 );
        LATIN1_ENTITIES.put( "infin",     (char)8734 );
        LATIN1_ENTITIES.put( "ang",       (char)8736 );
        LATIN1_ENTITIES.put( "and",       (char)8743 );
        LATIN1_ENTITIES.put( "or",        (char)8744 );
        LATIN1_ENTITIES.put( "cap",       (char)8745 );
        LATIN1_ENTITIES.put( "cup",       (char)8746 );
        LATIN1_ENTITIES.put( "int",       (char)8747 );
        LATIN1_ENTITIES.put( "there4",    (char)8756 );
        LATIN1_ENTITIES.put( "sim",       (char)8764 );
        LATIN1_ENTITIES.put( "cong",      (char)8773 );
        LATIN1_ENTITIES.put( "asymp",     (char)8776 );
        LATIN1_ENTITIES.put( "ne",        (char)8800 );
        LATIN1_ENTITIES.put( "equiv",     (char)8801 );
        LATIN1_ENTITIES.put( "le",        (char)8804 );
        LATIN1_ENTITIES.put( "ge",        (char)8805 );
        LATIN1_ENTITIES.put( "sub",       (char)8834 );
        LATIN1_ENTITIES.put( "sup",       (char)8835 );
        LATIN1_ENTITIES.put( "nsub",      (char)8836 );
        LATIN1_ENTITIES.put( "sube",      (char)8838 );
        LATIN1_ENTITIES.put( "supe",      (char)8839 );
        LATIN1_ENTITIES.put( "oplus",     (char)8853 );
        LATIN1_ENTITIES.put( "otimes",    (char)8855 );
        LATIN1_ENTITIES.put( "perp",      (char)8869 );
        LATIN1_ENTITIES.put( "sdot",      (char)8901 );
        LATIN1_ENTITIES.put( "lceil",     (char)8968 );
        LATIN1_ENTITIES.put( "rceil",     (char)8969 );
        LATIN1_ENTITIES.put( "lfloor",    (char)8970 );
        LATIN1_ENTITIES.put( "rfloor",    (char)8971 );
        LATIN1_ENTITIES.put( "lang",      (char)9001 );
        LATIN1_ENTITIES.put( "rang",      (char)9002 );
        LATIN1_ENTITIES.put( "loz",       (char)9674 );
        LATIN1_ENTITIES.put( "spades",    (char)9824 );
        LATIN1_ENTITIES.put( "clubs",     (char)9827 );
        LATIN1_ENTITIES.put( "hearts",    (char)9829 );
        LATIN1_ENTITIES.put( "diams",     (char)9830 );

        ALL_ENTITIES.putAll( CDATA_ENTITIES );
        ALL_ENTITIES.putAll( LATIN1_ENTITIES );

    }

}
