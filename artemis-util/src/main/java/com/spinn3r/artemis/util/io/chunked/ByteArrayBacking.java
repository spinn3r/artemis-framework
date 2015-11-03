package com.spinn3r.artemis.util.io.chunked;

/**
 * Byte array backing for a chunk which is similar to a ByteArrayOutputStream
 * but allows us to access the raw bytes directly for checksum and sync methods
 * which are faster and don't require intermediate copying of the data.
 */
public class ByteArrayBacking {

    /**
     * The buffer where data is stored.
     */
    protected byte buf[];

    /**
     * The number of valid bytes in the buffer.
     */
    protected int count;

    public ByteArrayBacking(int size) {

        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }

        buf = new byte[size];

    }

    /**
     * Writes the specified byte to this byte array output stream.
     *
     * @param   b   the byte to be written.
     */
    public void write(int b) {
        buf[count] = (byte) b;
        count += 1;
    }

    public void write(byte b[], int off, int len) {

        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }

        System.arraycopy(b, off, buf, count, len);
        count += len;

    }

}
