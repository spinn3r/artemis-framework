package com.spinn3r.artemis.util.io;

import com.spinn3r.artemis.util.text.UnicodeURLEncoder;

/**
 *
 */
public class PortableFilenameEncoder {

    public static String encodePathComponent(String name) {
        return UnicodeURLEncoder.encode(name);
    }

}
