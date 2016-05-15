package com.spinn3r.artemis.network.cookies.jar;

import com.spinn3r.artemis.json.JSON;
import com.spinn3r.artemis.network.cookies.CookieValueStore;

import java.io.File;
import java.io.IOException;

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

    public FileBackedCookieJar( File file ) throws IOException {

        super(JSON.fromJSON(CookieValueStore.class, file));

    }

}
