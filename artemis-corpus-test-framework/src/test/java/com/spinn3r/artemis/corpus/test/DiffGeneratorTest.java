package com.spinn3r.artemis.corpus.test;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DiffGeneratorTest {

    @Test
    public void testDiff0() throws Exception {

        String a = toUTF8( "/test0a.txt" );
        String b = toUTF8( "/test0b.txt" );

        String diff = DiffGenerator.diff( a, b );

        System.out.printf( "%s\n", diff );

    }

    public String toUTF8( String path ) throws IOException {
        byte[] data = ByteStreams.toByteArray( getClass().getResourceAsStream( path ) );

        return new String( data, Charsets.UTF_8 );
    }

}