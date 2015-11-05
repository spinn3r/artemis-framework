package com.spinn3r.artemis.schema.core;

import java.util.Comparator;

/**
 * Comparator for two byte arrays.
 */
public class ByteArrayComparator implements Comparator<byte[]> {

    @Override
    public int compare( byte[] v1, byte[] v2 ) {

        int len1 = v1.length;
        int len2 = v2.length;
        int n = Math.min(len1, len2);

        int i = 0;

        int k = i;
        final int lim = n + i;
        while (k < lim) {
            byte c1 = v1[k];
            byte c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }

        return len1 - len2;
    }


}
