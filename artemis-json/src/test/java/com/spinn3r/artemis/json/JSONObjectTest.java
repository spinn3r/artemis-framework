package com.spinn3r.artemis.json;

import com.spinn3r.artemis.util.misc.Files;
import org.junit.Test;


import java.io.IOException;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class JSONObjectTest {


    @Test
    public void test1() throws Exception {
        String path = "/objects1.json";
        Objects objects = parse( path );
        assertEquals( "Objects{integerValue=null}", objects.toString() );
    }

    @Test
    public void test2() throws Exception {
        String path = "/objects2.json";
        Objects objects = parse( path );
        assertEquals( "Objects{integerValue=100}", objects.toString() );
    }

    private Objects parse(String path) throws IOException {
        return JSON.fromJSON( Objects.class, Files.toUTF8( getClass().getResourceAsStream( path ) ) );
    }

}