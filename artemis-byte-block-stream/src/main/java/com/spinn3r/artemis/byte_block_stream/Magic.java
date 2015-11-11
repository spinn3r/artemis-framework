package com.spinn3r.artemis.byte_block_stream;

import java.nio.ByteBuffer;

/**
 *
 */
public class Magic {

    public static final ByteBuffer HEADER
      = ByteBuffer.wrap( new byte[] { (byte)'b', (byte)'b', (byte)'1' } );

}
