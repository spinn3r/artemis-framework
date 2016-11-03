package com.spinn3r.artemis.util.text;

import com.google.common.base.Charsets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Encode a URL with UTF8 and do not expose the UnsupportedEncodingException
 * since if we hard code UTF8 it won't ever happen making our code far less ugly.
 */
public class UnicodeURLEncoder {

    public static String encode(String data) {

        try {

            return URLEncoder.encode(data, Charsets.UTF_8.name());

        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoder not available");
        }

    }

}
