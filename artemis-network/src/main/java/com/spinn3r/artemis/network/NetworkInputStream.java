package com.spinn3r.artemis.network;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class NetworkInputStream extends InputStream {

    private InputStream delegate = null;

    private ResourceRequest request = null;

    /**
     * When specified, we throw an exception if we're downloading more than the
     * maximum number of acceptable bytes.
     *
     */
    private int maxContentLength = -1;

    /**
     * Number of bytes read from this input stream.
     */
    private int count = 0;

    public NetworkInputStream( InputStream delegate,  int maxContentLength, ResourceRequest request ) {
        this.delegate = delegate;
        this.maxContentLength = maxContentLength;
        this.request = request;
    }

    public int read() throws IOException {

        int result = delegate.read();

        ++count;

        assertMaxContentLength();

        return result;
    }

    public int read( byte buffer[] ) throws IOException {

        int _count = delegate.read( buffer );

        count += _count;

        assertMaxContentLength();

        return _count;

    }

    public int read( byte buffer[], int off, int len ) throws IOException {

        int _count = delegate.read( buffer, off, len );

        count += _count;

        assertMaxContentLength();

        return _count;

    }

    public long skip( long n ) throws IOException {

        long v = delegate.skip( n );

        return v;
    }

    /**
     * Assert that we haven't read too many bytes.
     *
     */
    private void assertMaxContentLength() throws IOException {

        if ( maxContentLength == -1 )
            return;

        if ( count > maxContentLength ) {

            throw new NetworkException( String.format( "Content is too large - %s: %s",
                                                       count, request.getResource() ) );

        }

    }

    @Override
    public int available() throws IOException {
        return delegate.available();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
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
