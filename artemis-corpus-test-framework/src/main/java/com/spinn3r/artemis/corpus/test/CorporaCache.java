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
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class CorporaCache {

    private static String ROOT = System.getProperty( "corpora-cache.root", "src/test/resources/" );

    private final Class<?> parent;

    private String basedir = "/corpora";

    private String extension = "dat";

    // FIXME: do not make this use compression by default

    // FIXME: make the NetworkCache use compression by default

    // FIXME: when we update documents, we accidentally create a new .gz file
    // and NOT update the existing non-gz file.   

    public CorporaCache(Class<?> parent) {
        this.parent = parent;
    }

    public CorporaCache(Class<?> parent, String basedir) {
        this.parent = parent;
        this.basedir = basedir;
    }

    public CorporaCache(Class<?> parent, String basedir, String extension) {
        this.parent = parent;
        this.basedir = basedir;
        this.extension = extension;
    }

    public boolean contains(String key) {

        try {
            ImmutableList<String> paths = computePaths(key);
            findExistingFile(paths);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }

    }

    public void write(String key, String data) throws IOException {

        checkNotNull(data, "data");

        String path = computePath( key ) + ".gz";

        File file = new File( ROOT, path );

        byte[] bytes = data.getBytes( Charsets.UTF_8 );

        System.out.printf( "Writing %,d bytes of cache data to: %s\n", bytes.length, file.getAbsolutePath() );

        Files.createDirectories( Paths.get( file.getParent() ) );

        try(OutputStream out = new GZIPOutputStream(new FileOutputStream(file)) ) {
            out.write( bytes );
        }

    }

    public String read(String key) throws IOException {

        ImmutableList<String> paths = computePaths(key);

        File file = findExistingFile(paths);

        System.out.printf( "CorporaCache reading from: %s\n", file.getAbsolutePath() );

        try ( InputStream is = createInputStream(file) ) {
            byte[] data = ByteStreams.toByteArray( is );
            return new String( data, Charsets.UTF_8 );
        }

    }

    private InputStream createInputStream(File file) throws IOException {

        if(file.getName().endsWith(".gz")) {
            return new GZIPInputStream(new FileInputStream(file));
        } else {
            return new FileInputStream(file);
        }

    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    protected File findExistingFile(ImmutableList<String> paths) throws FileNotFoundException {

        for (String path : paths) {
            File file = new File( ROOT, path );
            if (file.exists()) {
                return file;
            }
        }

        throw new FileNotFoundException("No files exist for paths: " + paths);

    }

    public String computePath( String key ) {
        return String.format( "%s/%s.%s.%s", basedir, parent.getName(), key, getExtension() );
    }

    public ImmutableList<String> computePaths(String key) {
        String path = computePath(key);
        return ImmutableList.of(path, path + ".gz");
    }

}


