package com.spinn3r.artemis.http.servlets;

import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class IndexWriterTest {

    @Test
    public void testToHTML() throws Exception {

        Map<String,ServletHolder> servlets = new HashMap<>();

        servlets.put( "/foo", new ServletHolder( HelloServlet.class ) );
        servlets.put( "/fooasdf", new ServletHolder( ChaosServlet.class ) );

        IndexWriter indexWriter = new IndexWriter( servlets, "localhost", "api" );

        //assertEquals( "", indexWriter.toHTML() );

        try( FileOutputStream fos = new FileOutputStream( "/tmp/index-writer-test.html" ) ) {
            fos.write( indexWriter.toHTML().getBytes() );
        }

    }


}