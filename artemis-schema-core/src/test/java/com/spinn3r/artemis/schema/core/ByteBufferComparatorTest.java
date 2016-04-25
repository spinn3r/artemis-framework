package com.spinn3r.artemis.schema.core;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ByteBufferComparatorTest {

    @Test
    public void testCompare() throws Exception {

        ByteBufferComparator byteBufferComparator = new ByteBufferComparator();

        ByteBuffer hello0 = ByteBuffer.wrap( "hello0".getBytes() );
        ByteBuffer hello1 = ByteBuffer.wrap( "hello1".getBytes() );

        assertEquals( -1, byteBufferComparator.compare( null, hello0 ) );
        assertEquals( 1, byteBufferComparator.compare( hello0, null ) );
        assertEquals( 0, byteBufferComparator.compare( null, null ) );

        assertEquals( 1, byteBufferComparator.compare( hello0, null ) );

        assertEquals( 0, byteBufferComparator.compare( hello0, hello0 ) );
        assertEquals( -1, byteBufferComparator.compare( hello0, hello1 ) );

    }

}