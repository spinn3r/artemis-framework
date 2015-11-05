package com.spinn3r.artemis.network;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Safe character set lookup to find the CORRECT Java charset to use from
 * charsets to use in the wild. For example, big5 is NOT a valid Java charset
 * but Big5 is... In this case we support case insensitivity.  The charset
 * win-1251 is NOT valid in Java but windows-1251 is perfectly valid.
 *
 */
public class SafeCharsetLookup {

    private static Map<String,String> lookup = new HashMap<>();

    public static String lookup( String name ) {

        if ( name == null )
            return null;

        String key = name.toLowerCase();

        if ( lookup.containsKey( key ) ) {
            return lookup.get( key );
        }

        //this is a hack for certain broken sites.  None is NOT a valid charset
        //name but it's sometimes used for some reason.
        if ( "none".equals( name )  )
            return null;

        return name;

    }

    static {

        Map<String,Charset> charsets = Charset.availableCharsets();

        for (String current : charsets.keySet()) {
            lookup.put( current.toLowerCase(), current );
        }

        //note that the keys should be lowercase here.
        lookup.put( "win-1250", "windows-1250" );
        lookup.put( "win-1251", "windows-1251" );
        lookup.put( "win-1252", "windows-1252" );
        lookup.put( "win-1253", "windows-1253" );
        lookup.put( "win-1254", "windows-1254" );
        lookup.put( "win-1255", "windows-1255" );
        lookup.put( "win-1256", "windows-1256" );
        lookup.put( "win-1257", "windows-1257" );
        lookup.put( "win-1258", "windows-1258" );
        lookup.put( "win-31j",  "windows-31j" );

        lookup.put( "window-1250", "windows-1250" );
        lookup.put( "window-1251", "windows-1251" );
        lookup.put( "window-1252", "windows-1252" );
        lookup.put( "window-1253", "windows-1253" );
        lookup.put( "window-1254", "windows-1254" );
        lookup.put( "window-1255", "windows-1255" );
        lookup.put( "window-1256", "windows-1256" );
        lookup.put( "window-1257", "windows-1257" );
        lookup.put( "window-1258", "windows-1258" );
        lookup.put( "window-31j",  "windows-31j" );

        lookup.put( "iso-859-1",     "ISO-8859-1" );

        lookup.put( "windows-utf-8", "UTF-8" );
        lookup.put( "utf8",          "UTF-8" );
        lookup.put( "utf16",         "UTF-16" );

    }

}
