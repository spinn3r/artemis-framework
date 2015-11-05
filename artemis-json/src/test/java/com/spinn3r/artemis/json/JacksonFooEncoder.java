package com.spinn3r.artemis.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.spinn3r.artemis.util.io.FastByteArrayOutputStream;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 */
public class JacksonFooEncoder {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private final FastByteArrayOutputStream fastByteArrayOutputStream;

    public JacksonFooEncoder(FastByteArrayOutputStream fastByteArrayOutputStream) {
        this.fastByteArrayOutputStream = fastByteArrayOutputStream;
    }

    public ByteBuffer encode( Foo foo ) throws IOException {

        try ( JsonGenerator generator = JSON_FACTORY.createGenerator( fastByteArrayOutputStream, JsonEncoding.UTF8 ); ) {

            generator.disable( JsonGenerator.Feature.AUTO_CLOSE_TARGET );

            generator.writeStartObject();

            if (foo.getFirstName() != null) {
                generator.writeStringField( "first_name", foo.getFirstName() );
            }

            if (foo.getLastName() != null) {
                generator.writeStringField( "last_name", foo.getLastName() );
            }

            if (foo.getAddress() != null) {
                generator.writeStringField( "address", foo.getAddress() );
            }

            generator.writeEndObject();

        }

        return fastByteArrayOutputStream.toByteBuffer();

    }


}
