package com.spinn3r.artemis.resource_finder.references;

import com.spinn3r.artemis.resource_finder.ResourceHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class FileResourceReference extends ResourceReference {

    private final File file;

    public FileResourceReference(ResourceHolder resourceHolder, String path, File file) {
        super( resourceHolder, path );
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream( file );
    }

    @Override
    public String toString() {
        return String.format( "%s (%s)", getPath(), file.getAbsolutePath() );
    }

}
