package com.spinn3r.artemis.util.misc;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LongsTest {

    @Test
    public void testByteArrayEncoding() throws Exception {

        long value = 750803762000000000L;

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(value);
        byte[] data = buffer.array();

        String encoded = Base64.encode(data);

        System.out.printf("%s\n",encoded);

    }
}