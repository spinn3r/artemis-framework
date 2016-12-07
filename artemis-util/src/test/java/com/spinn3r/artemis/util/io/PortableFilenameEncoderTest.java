package com.spinn3r.artemis.util.io;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class PortableFilenameEncoderTest {


    @Test
    public void encodePathComponent() throws Exception {
        assertEquals("hello", PortableFilenameEncoder.encodePathComponent("hello"));
        assertEquals("%E5%92%8C%E9%A3%9F", PortableFilenameEncoder.encodePathComponent("和食"));
    }

}