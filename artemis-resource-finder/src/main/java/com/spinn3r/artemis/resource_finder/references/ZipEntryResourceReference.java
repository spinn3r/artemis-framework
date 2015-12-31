package com.spinn3r.artemis.resource_finder.references;

import com.spinn3r.artemis.resource_finder.ResourceHolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 */
public class ZipEntryResourceReference extends ResourceReference {

    private final File zipFile;

    private final ZipEntry zipEntry;

    public ZipEntryResourceReference(ResourceHolder resourceHolder, String path, File zipFile, ZipEntry zipEntry) {
        super( resourceHolder, path );
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
    }

    @Override
    public InputStream getInputStream() throws IOException {

        try(ZipFile zip = new ZipFile( zipFile  ) ) {
            return zip.getInputStream( zipEntry );
        }

    }

    @Override
    public String toString() {
        return String.format( "%s (%s)", getPath(), zipFile.getAbsolutePath() );
    }

}



