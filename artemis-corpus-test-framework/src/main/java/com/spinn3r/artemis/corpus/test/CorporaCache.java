/**
 * boilerpipe
 *
 * Copyright (c) 2009, 2014 Christian Kohlschütter
 *
 * The author licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spinn3r.artemis.corpus.test;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class CorporaCache {

    private static String ROOT = System.getProperty( "corpora-cache.root", "src/test/resources/" );

    private String extension = "dat";

    private final Class<?> parent;

    private String basedir = "/corpora";

    public CorporaCache(Class<?> parent) {
        this.parent = parent;
    }

    public CorporaCache(Class<?> parent, String basedir) {
        this.parent = parent;
        this.basedir = basedir;
    }

    public boolean contains( String key ) {
        String path = computePath( key );
        File file = new File( ROOT, path );
        boolean result = file.exists();
        return result;
    }

    public void write( String key, String data ) throws IOException {

        checkNotNull(data, "data");

        String path = computePath( key );

        File file = new File( ROOT, path );

        byte[] bytes = data.getBytes( Charsets.UTF_8 );

        System.out.printf( "Writing %,d bytes of cache data to: %s\n", bytes.length, file.getAbsolutePath() );

        Files.createDirectories( Paths.get( file.getParent() ) );

        try( OutputStream out = new FileOutputStream( file ) ) {

            out.write( bytes );

        }

    }

    @Nullable
    public String read(String key) throws IOException {

        String path = computePath( key );

        File file = new File( ROOT, path );

        if(!file.exists())
            return null;

        System.out.printf( "CorporaCache reading from: %s\n", file.getAbsolutePath() );

        try ( InputStream is = new FileInputStream(file) ) {

            byte[] data = ByteStreams.toByteArray( is );

            return new String( data, Charsets.UTF_8 );

        }

    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String computePath( String key ) {
        return String.format( "%s/%s.%s.%s", basedir, parent.getName(), key, getExtension() );
    }

}


