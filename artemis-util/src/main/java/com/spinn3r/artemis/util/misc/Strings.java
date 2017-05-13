package com.spinn3r.artemis.util.misc;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.guava.ImmutableCollectors;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Strings {

    private static final int UNIFIED_DIFF_CONTEXT_SIZE = 5;

    /**
     * Compare two strings and generated a unified diff.
     */
    public static String diff( String original, String revised ) {

        List<String> originalLines = Arrays.asList( original.split( "\n" ) );
        List<String> revisedLines  = Arrays.asList( revised.split( "\n" ) );

        // Compute diff. Get the Patch object. Patch is the container for computed deltas.
        Patch patch = DiffUtils.diff( originalLines, revisedLines );

        List<String> unifiedDiffs = DiffUtils.generateUnifiedDiff( original, revised, originalLines, patch, UNIFIED_DIFF_CONTEXT_SIZE );

        StringBuilder buff = new StringBuilder();

        for (String unifiedDiff : unifiedDiffs) {
            buff.append( unifiedDiff );
            buff.append( "\n" );
        }

        return buff.toString();

    }

    public static int count( String source, String regex ) {

        int result = 0;

        Pattern pattern = Pattern.compile( regex );

        Matcher matcher = pattern.matcher( source );

        while( matcher.find() ) {
            ++result;
        }

        return result;

    }

    public static boolean nonEmpty(String value) {
        return ! empty(value);
    }

    public static boolean empty( String value ) {

        return value == null || "".equals(value);

    }

    public static boolean emptyWithTrimming( String value ) {

        return value == null || "".equals(value.trim());

    }

    public static String truncate( String value, int maxLength ) {

        if ( value == null ) {
            return null;
        }

        if ( value.length() < maxLength ) {
            return value;
        }

        return value.substring( 0, maxLength );

    }

    public static String repeat( String value , int iterations ) {

        StringBuilder buff = new StringBuilder( value.length() * iterations );

        for (int i = 0; i < iterations; i++) {
            buff.append( value );
        }

        return buff.toString();

    }

    public static List<String> split( String string, String regex ) {
        return Arrays.asList(string.split( regex ));

    }

    public static String[] toArray( List<String> input ) {

        String[] result = new String[input.size()];
        return input.toArray( result );

    }

    /**
     * @Deprecated use toStrings instead.
     */
    @Deprecated
    public static List<String> toList( String[] strings ) {

        List<String> list = Lists.newArrayList();

        Collections.addAll( list, strings );

        return list;

    }

    public static ImmutableList<String> toStrings(Object... objects) {

        List<String> list = Lists.newArrayList();

        for (Object object : objects) {

            if (object == null)
                throw new NullPointerException("Null objects not allowed");

            list.add(object.toString());
        }

        return ImmutableList.copyOf(list);

    }

    public static List<String> toLines( String data ) {
        return Arrays.asList( data.split( "\n" ) );
    }

    /**
     * Create some padding for use in ASCII art and formatting.
     */
    public static String padding( char c , int length ) {

        char[] chars = new char[length];

        for (int i = 0; i < length; i++) {
            chars[i] = c;
        }

        return new String( chars );

    }

    /**
     * Compute a null safe length and return zero if the value is null.
     */
    public static int lengthNullSafe( String value ) {

        if ( value == null )
            return 0 ;

        return value.length();

    }

    /**
     * Create fixed width chunks.
     */
    public static ImmutableList<String> chunk(String text, int width) {

        return Splitter.fixedLength(width).splitToList(text)
                .stream()
                .filter( current -> current.length() == width )
                .collect(ImmutableCollectors.toImmutableList());
    }

    /**
     * Safely convert a String to an Integer and minimize Exceptions thrown.  If
     * the string value does not represent an integer we return null.
     */
    public static <T> Integer toInteger(T value ) {

        if ( value != null && value.toString().matches( "-?[0-9]+" ) ) {

            try {
                return Integer.parseInt( value.toString() );
            } catch ( NumberFormatException e ) {
                return null;
            }

        }

        return null;

    }

    public static InputStream toInputStream(String text) {
        return toInputStream(text, Charsets.UTF_8);
    }

    /**
     * Convert the given text to an InputStream so that it can be read as IO.
     */
    public static InputStream toInputStream(String text, Charset charset) {
        return new ByteArrayInputStream(text.getBytes(charset));
    }

    public static String head(String text, int offset, int length) {

        // FIXME: bug here where we are -1 ...
        return text.substring(offset, Math.min(offset+length, text.length()));
    }

}
