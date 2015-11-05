package com.spinn3r.artemis.schema.core;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import static org.junit.Assert.*;

public class JSONMapTest {

    @Test
    public void testWriteTwoMembers() throws Exception {

        Map<String,Long> map = new NoNullMap<>();

        map.put( "one", 1L );
        map.put( "two", 2L );

        String content = test( map );

        assertEquals( "{\n" +
                        "  \"map\" : {\n" +
                        "    \"one\" : 1,\n" +
                        "    \"two\" : 2\n" +
                        "  }\n" +
                        "}",
                      content );

    }

    @Test
    public void testWriteOneMember() throws Exception {

        Map<String,Long> map = new NoNullMap<>();

        map.put( "one", 1L );

        String content = test( map );

        assertEquals( "{\n" +
                        "  \"map\" : {\n" +
                        "    \"one\" : 1\n" +
                        "  }\n" +
                        "}",
                      content );

    }

    @Test
    public void testWriteNoMembers() throws Exception {

        Map<String,Long> map = new NoNullMap<>();

        String content = test( map );

        assertEquals( "{ }",
                      content );

    }


    private String test( Map<String,Long> map ) throws Exception {

        JsonFactory jfactory = new JsonFactory();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try ( JsonGenerator generator = jfactory.createGenerator( out, JsonEncoding.UTF8 ) ) {

            generator.useDefaultPrettyPrinter();

            generator.disable( JsonGenerator.Feature.AUTO_CLOSE_TARGET );

            generator.writeStartObject();

            JSON.writeStringLongMap( generator, "map", map );

            generator.writeEndObject();

        }

        return new String( out.toByteArray(), "UTF-8");

    }


}