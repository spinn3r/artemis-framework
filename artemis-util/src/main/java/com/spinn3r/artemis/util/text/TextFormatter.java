package com.spinn3r.artemis.util.text;

import java.util.regex.Pattern;

/**
 *
 */
public class TextFormatter {

    public static String indent(String input) {
        return indent( input , "    " );
    }

    /**
     * Indent all the lines in the input and return a new string.
     */
    public static String indent(String input, String padding) {

        StringBuilder buff = new StringBuilder();

        for (String current : input.split( "\n" ) ) {
            buff.append( padding );
            buff.append( current );
            buff.append( "\n" );
        }

        return buff.toString();

    }

    public static String unindent(String input) {

        return input.replaceAll("(?m)^\\s+", "");

    }

    public static String vertical( String text ) {

        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt( i );

            buff.append( c );

            if ( i > 0 && i % 80 == 0 ) {
                buff.append( "\n" );
            }

        }

        return buff.toString();

    }

    public static String wrap(String str) {
        return wrap( str, 80, "\n", true );
    }

    public static String wrap(String str,  int wrapLength, String newLineStr, boolean wrapLongWords) {

        // TODO: borrowed from commons-lang Wordutils.wrap

        if(str == null) {
            return null;
        } else {
            if(newLineStr == null) {
                newLineStr = "\n";
            }

            if(wrapLength < 1) {
                wrapLength = 1;
            }

            int inputLineLength = str.length();
            int offset = 0;
            StringBuffer wrappedLine = new StringBuffer(inputLineLength + 32);

            while(inputLineLength - offset > wrapLength) {
                if(str.charAt(offset) == 32) {
                    ++offset;
                } else {
                    int spaceToWrapAt = str.lastIndexOf(32, wrapLength + offset);
                    if(spaceToWrapAt >= offset) {
                        wrappedLine.append(str.substring(offset, spaceToWrapAt));
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt + 1;
                    } else if(wrapLongWords) {
                        wrappedLine.append(str.substring(offset, wrapLength + offset));
                        wrappedLine.append(newLineStr);
                        offset += wrapLength;
                    } else {
                        spaceToWrapAt = str.indexOf(32, wrapLength + offset);
                        if(spaceToWrapAt >= 0) {
                            wrappedLine.append(str.substring(offset, spaceToWrapAt));
                            wrappedLine.append(newLineStr);
                            offset = spaceToWrapAt + 1;
                        } else {
                            wrappedLine.append(str.substring(offset));
                            offset = inputLineLength;
                        }
                    }
                }
            }

            wrappedLine.append(str.substring(offset));
            return wrappedLine.toString();
        }    }

}
