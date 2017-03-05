package com.spinn3r.artemis.network.cookies.jar;

import com.spinn3r.artemis.json.JSON;
import com.spinn3r.artemis.json.JSONS;
import com.spinn3r.artemis.network.cookies.CookieValueMap;
import com.spinn3r.artemis.network.cookies.CookieValueStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple file backed cookie map which is just an list of cookies storeed as maps.
 *
 * For example:
 *
 * [ { "foo": "bar" } ]
 *
 * Would be a backed file with one entry having a cookie mapping of foo to bar.
 */
public class FileBackedCookieJar extends BackedCookieJar {

    public FileBackedCookieJar(File file, CookieJarReference.Format format) throws IOException {
        super(parse(file, format));
    }

    public FileBackedCookieJar(InputStream inputStream, CookieJarReference.Format format) throws IOException {
        super(parse(inputStream, format));
    }

    public static CookieValueStore parse(File file, CookieJarReference.Format format) throws IOException {
        try(InputStream inputStream = new FileInputStream(file)) {
            return parse(inputStream, format);
        }
    }

    public static CookieValueStore parse(InputStream inputStream, CookieJarReference.Format format) throws IOException {

        switch (format) {

            case JSON:
                return JSON.deserialize(CookieValueStore.class, inputStream);

            case JSONS:
                return new CookieValueStore(JSONS.parse(CookieValueMap.class, inputStream));

            default:
                throw new IOException("Wrong format: " + format);

        }

    }

}
