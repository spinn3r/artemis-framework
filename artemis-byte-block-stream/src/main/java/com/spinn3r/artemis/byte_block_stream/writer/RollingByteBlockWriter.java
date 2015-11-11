package com.spinn3r.artemis.byte_block_stream.writer;

import com.spinn3r.artemis.byte_block_stream.ByteBlock;

import java.io.IOException;

/**
 *
 */
public class RollingByteBlockWriter implements ByteBlockWriter {

    private final ByteBlockWriterFactory byteBlockWriterFactory;
    private final int maxCapacity;

    private ByteBlockWriter byteBlockWriter;

    private int written;

    public RollingByteBlockWriter(ByteBlockWriterFactory byteBlockWriterFactory) throws IOException {
        this( byteBlockWriterFactory, 100_000_000 );
    }

    public RollingByteBlockWriter(ByteBlockWriterFactory byteBlockWriterFactory, int maxCapacity ) throws IOException {
        this.byteBlockWriterFactory = byteBlockWriterFactory;
        this.maxCapacity = maxCapacity;

        byteBlockWriterFactory.init();
        rollover();

    }

    @Override
    public void write(ByteBlock byteBlock) throws IOException {

        if ( written > maxCapacity ) {
            rollover();
        }

        int length = byteBlock.length();

        byteBlockWriter.write( byteBlock );
        written += length;

    }

    @Override
    public void close() throws IOException {

        if ( byteBlockWriter != null ) {
            byteBlockWriter.close();
        }

    }

    private void rollover() throws IOException {

        if ( byteBlockWriter != null ) {
            byteBlockWriter.close();
        }

        byteBlockWriter = byteBlockWriterFactory.create();
        written = 0;

    }

}
