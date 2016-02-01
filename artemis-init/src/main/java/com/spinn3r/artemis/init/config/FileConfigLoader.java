package com.spinn3r.artemis.init.config;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.NoSuchFileException;

/**
 *
 */
public class FileConfigLoader implements ConfigLoader {

    private File directory;

    public FileConfigLoader( String directory ) {
        this( new File( directory ) );
    }

    public FileConfigLoader(File directory) {
        this.directory = directory;
    }

    @Override
    public URL getResource(Class<?> clazz, String path) {

        File file = new File( directory, path );

        if ( ! file.exists() )
            return null;

        try {

            return file.toURI().toURL();

        } catch (MalformedURLException e) {

            // this will never happen in practice
            throw new RuntimeException( e );

        }

    }

    @Override
    public String toString() {
        return "FileConfigLoader{" +
                 "directory=" + directory +
                 '}';
    }

}
