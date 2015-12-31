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
        ZipFile zip = new ZipFile( zipFile  );
        return new ZippedInputStream( zip, zip.getInputStream( zipEntry ) );
    }

    @Override
    public String toString() {
        return String.format( "%s (%s)", getPath(), zipFile.getAbsolutePath() );
    }

    private static class ZippedInputStream extends InputStream {

        private final ZipFile zipFile;

        private final InputStream delegate;

        public ZippedInputStream(ZipFile zipFile, InputStream delegate) {
            this.zipFile = zipFile;
            this.delegate = delegate;
        }

        @Override
        public int read() throws IOException {
            return delegate.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return delegate.read( b );
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return delegate.read( b, off, len );
        }

        @Override
        public long skip(long n) throws IOException {
            return delegate.skip( n );
        }

        @Override
        public int available() throws IOException {
            return delegate.available();
        }

        @Override
        public void close() throws IOException {
            delegate.close();
            zipFile.close();
        }

        @Override
        public void mark(int readlimit) {
            delegate.mark( readlimit );
        }

        @Override
        public void reset() throws IOException {
            delegate.reset();
        }

        @Override
        public boolean markSupported() {
            return delegate.markSupported();
        }

    }

}



