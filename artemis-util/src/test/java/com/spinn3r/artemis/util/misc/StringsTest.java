package com.spinn3r.artemis.util.misc;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void testEmpty() throws Exception {

        Assert.assertEquals( true, Strings.empty( null ) );
        assertEquals( true, Strings.empty( "" ) );

        assertEquals( false, Strings.empty( "asdf" ) );

    }

    @Test
    public void testTruncate() throws Exception {
        assertEquals( "", Strings.truncate( "", 3 ) );
        assertEquals( "1", Strings.truncate( "1", 3 ) );
        assertEquals( "12", Strings.truncate( "12", 3 ) );
        assertEquals( "123", Strings.truncate( "123", 3 ) );
        assertEquals( "123", Strings.truncate( "1234", 3 ) );
    }

    @Test
    public void testChunk() throws Exception {

        assertEquals("[]", Strings.chunk("", 10).toString());
        assertEquals("[x]", Strings.chunk("x", 1).toString());
        assertEquals("[x, x]", Strings.chunk("xx", 1).toString());
        assertEquals("[xx]", Strings.chunk("xxx", 2).toString());
        assertEquals("[xx, xx]", Strings.chunk("xxxx", 2).toString());

    }
    
}