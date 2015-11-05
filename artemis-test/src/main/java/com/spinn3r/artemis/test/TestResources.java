package com.spinn3r.artemis.test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class TestResources {

    public static String getResourceAsString( String path ) throws IOException {
        return Resources.toString( TestResources.class.getResource( path ), Charsets.UTF_8 );
    }

    /**
     * Find the first directory that exists. Useful for testing and finding
     * out where maven is running from since it used cwd during module runs.
     *
     * @param paths
     * @return
     */
    public static String findFirstDirectory( String... paths ) throws RuntimeException {

        for (String path : paths) {

            File dir = new File( path );

            if ( dir.isDirectory() ) {
                return path;
            }

        }

        throw new RuntimeException( "No paths found... tried: " + Lists.newArrayList( paths ) );

    }
}
