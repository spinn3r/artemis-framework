package com.spinn3r.artemis.network;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTokenizerTest {

    @Test
    public void testDoRemoveWWWPart() throws Exception {

        assertEquals( "http://cnn.com", ResourceTokenizer.doRemoveWWWPart( "http://cnn.com" ) );
        assertEquals( "http://cnn.com", ResourceTokenizer.doRemoveWWWPart( "http://www.cnn.com" ) );
        assertEquals( "https://cnn.com", ResourceTokenizer.doRemoveWWWPart( "https://www.cnn.com" ) );

    }

}