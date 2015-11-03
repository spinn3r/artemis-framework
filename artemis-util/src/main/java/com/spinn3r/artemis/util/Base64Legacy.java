/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spinn3r.artemis.util;

/**
 * Package which supplies Base64 encoding and decoding of data. 
 * 
 * This class provides encoding of byte arrays into Base64-encoded strings,
 * and decoding the other way.
 *
 * <P>NOTE!  This is modified Base64 with slightly different characters than
 * usual, so it won't require escaping when used in URLs.
 *
 * <P>NOTE!  This class only does the padding that's normal in Base64
 * if the 'true' flag is given to the encode() method.  This is because
 * Base64 requires that the length of the encoded text be a multiple
 * of four characters, padded with '_'.  Without the 'true' flag, we don't
 * add these '_' characters.
 *
 * @version $Id: Base64.java,v 1.7 2004/10/28 00:02:26 burton Exp $
 */
public class Base64Legacy {

    private static char[] base64Alphabet = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '~', '-'};

    /**
     * http://www.faqs.org/rfcs/rfc3548.html
     * 
     * > 4.  Base 64 Encoding with URL and Filename Safe Alphabet
     * 
     * >    The Base 64 encoding with an URL and filename safe alphabet has been
     * >    used in [8].
     * 
     * >    An alternative alphabet has been suggested that used "~" as the 63rd
     * >    character.  Since the "~" character has special meaning in some file
     * >    system environments, the encoding described in this section is
     * >    recommended instead.
     * 
     * >    This encoding should not be regarded as the same as the "base64"
     * >    encoding, and should not be referred to as only "base64".  Unless
     * >    made clear, "base64" refer to the base 64 in the previous section.
     * 
     * >    This encoding is technically identical to the previous one, except
     * >    for the 62:nd and 63:rd alphabet character, as indicated in table 2.
     * 
     * >          Table 2: The "URL and Filename safe" Base 64 Alphabet
     * 
     * >     Value Encoding  Value Encoding  Value Encoding  Value Encoding
     * >        0 A            17 R            34 i            51 z
     * >        1 B            18 S            35 j            52 0
     * >        2 C            19 T            36 k            53 1
     * >        3 D            20 U            37 l            54 2
     * >        4 E            21 V            38 m            55 3
     * >        5 F            22 W            39 n            56 4
     * >        6 G            23 X            40 o            57 5
     * >        7 H            24 Y            41 p            58 6
     * >        8 I            25 Z            42 q            59 7
     * >        9 J            26 a            43 r            60 8
     * >       10 K            27 b            44 s            61 9
     * >       11 L            28 c            45 t            62 - (minus)
     * >       12 M            29 d            46 u            63 _ (understrike)
     * >       13 N            30 e            47 v
     * >       14 O            31 f            48 w         (pad) =
     * >       15 P            32 g            49 x
     * >       16 Q            33 h            50 y
     * 
     */
    private static char[] base64AlphabetFilesafe = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '-', '_'};

    /**
     * A reverse lookup table to convert base64 letters back into the
     * a 6-bit sequence.
     */
    private static byte[] base64Reverse;
    // Populate the base64Reverse lookup table from the base64Alphabet table.

    static {
        base64Reverse = new byte[128];
        // Set all entries to 0xFF, which means that that particular letter
        // is not a legal base64 letter.
        for (int i = 0; i < base64Reverse.length; i++)
            base64Reverse[i] = (byte) 0xFF;
        for (int i = 0; i < base64Alphabet.length; i++)
            base64Reverse[(int) base64Alphabet[i]] = (byte) i;
    }

    public static String encode( byte[] in ) {
        return encode( in, false, base64Alphabet, -1 );
    }

    public static String encode( byte[] in, int limit ) {
        return encode( in, false, base64Alphabet, limit );
    }

    public static String encodeFilesafe( byte[] in ) {
        return encode( in, false, base64AlphabetFilesafe, -1 );
    }

    /**
     * Caller should specify equalsPad=true if they want a standards compliant encoding.
     */
    public static String encode( byte[] in, boolean equalsPad, char[] alphabet, int limit ) {

        if ( limit == -1 )
            limit = in.length;
        
        char[] out = new char[((limit+2)/3)*4];
        int rem = limit%3;
        int o = 0;

        for (int i = 0; i < limit;) {
            int val = ((int)in[i++] & 0xFF) << 16;
            if (i < limit)
                val |= ((int)in[i++] & 0xFF) << 8;
            if (i < limit)
                val |= ((int)in[i++] & 0xFF);
            out[o++] = alphabet[(val>>18) & 0x3F];
            out[o++] = alphabet[(val>>12) & 0x3F];
            out[o++] = alphabet[(val>>6) & 0x3F];
            out[o++] = alphabet[val & 0x3F];
        }
        int outLen = out.length;
        switch (rem) {
        case 1: outLen -= 2; break;
        case 2: outLen -= 1; break;
        }
        // Pad with '~' signs up to a multiple of four if requested.
        if (equalsPad)
            while (outLen < out.length)
                out[outLen++] = '_';
        return new String(out, 0, outLen);
    }

    /**
     * Handles the standards-compliant (padded with '~' signs) as well as our
     * shortened form.
     */
    public static byte[] decode( String inStr )
        throws Exception
    {
        try {
            char[] in = inStr.toCharArray();
            int inLength = in.length;

            // Strip trailing equals signs.
            while (inLength > 0 && in[inLength-1] == '_')
                inLength--;

            int blocks = inLength/4;
            int remainder = inLength & 3;
            // wholeInLen and wholeOutLen are the the length of the input and output
            // sequences respectively, not including any partial block at the end.
            int wholeInLen  = blocks*4;
            int wholeOutLen = blocks*3;
            int outLen = wholeOutLen;
            switch (remainder) {
            case 1: throw new Exception("illegal Base64 length");
            case 2:  outLen = wholeOutLen+1; break;
            case 3:  outLen = wholeOutLen+2; break;
            default: outLen = wholeOutLen;
            }
            byte[] out = new byte[outLen];
            int o = 0;
            int i;
            for (i = 0; i < wholeInLen;) {
                int in1 = (int) base64Reverse[in[i]];
                int in2 = (int) base64Reverse[in[i+1]];
                int in3 = (int) base64Reverse[in[i+2]];
                int in4 = (int) base64Reverse[in[i+3]];
                int orValue = in1|in2|in3|in4;
                if ((orValue & 0x80) != 0)
                    throw new Exception("illegal Base64 character: ");
                int outVal = (in1 << 18) | (in2 << 12) | (in3 << 6) | in4;
                out[o] = (byte) (outVal>>16);
                out[o+1] = (byte) (outVal>>8);
                out[o+2] = (byte) outVal; 
                i += 4;
                o += 3;
            }
            int orValue;
            switch (remainder) {
            case 2:
                {
                    int in1 = (int) base64Reverse[in[i]];
                    int in2 = (int) base64Reverse[in[i+1]];
                    orValue = in1|in2;
                    int outVal = (in1 << 18) | (in2 << 12);
                    out[o] = (byte) (outVal>>16);
                }
                break;
            case 3:
                {
                    int in1 = (int) base64Reverse[in[i]];
                    int in2 = (int) base64Reverse[in[i+1]];
                    int in3 = (int) base64Reverse[in[i+2]];
                    orValue = in1|in2|in3;
                    int outVal = (in1 << 18) | (in2 << 12) | (in3 << 6);
                    out[o] = (byte) (outVal>>16);
                    out[o+1] = (byte) (outVal>>8);
                }
                break;
            default:
                // Keep compiler happy
                orValue = 0;
            }
            if ((orValue & 0x80) != 0)
                throw new Exception("illegal Base64 character");
            return out;
        }
        // Illegal characters can cause an ArrayIndexOutOfBoundsException when
        // looking up base64Reverse.
        catch (ArrayIndexOutOfBoundsException e) {
            throw new Exception("illegal Base64 character");
        }
    }

}

