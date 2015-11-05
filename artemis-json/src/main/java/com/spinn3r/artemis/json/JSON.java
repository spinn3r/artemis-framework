package com.spinn3r.artemis.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

/**
 *
 */
public class JSON {

    /**
     * Convert the object to JSON but also make it so that each arrays are pretty
     * printed one entry per line.
     *
     */
    public static String toJSON( Object obj ) {

        try {

            ObjectMapper objectMapper = new ObjectMapper( new Factory() );

            objectMapper.configure( SerializationFeature.INDENT_OUTPUT, true );
            objectMapper.disable( SerializationFeature.FAIL_ON_EMPTY_BEANS );

            return objectMapper.writeValueAsString( obj );

        } catch (JsonProcessingException e) {
            throw new RuntimeException( e );
        }

    }

    public static void toJSON( Object obj, OutputStream outputStream  ) throws IOException {

        try {

            ObjectMapper objectMapper = new ObjectMapper( new Factory() );

            objectMapper.configure( SerializationFeature.INDENT_OUTPUT, true );
            objectMapper.disable( SerializationFeature.FAIL_ON_EMPTY_BEANS );

            objectMapper.writeValue( outputStream, obj );

        } catch (JsonProcessingException e) {
            throw new RuntimeException( e );
        }

    }



    public static <T> T fromJSON( Class<T> clazz, String content ) {

        if ( content == null )
            throw new NullPointerException( "content" );

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue( content, clazz );

            /*
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.getFactory().enable( com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS );
            objectMapper.setPropertyNamingStrategy( new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy() );
            return objectMapper.readValue( content, clazz );
            */

        } catch (IOException e) {
            throw new RuntimeException( e );
        }

    }

    public static <T> T fromJSON( Class<T> clazz, InputStream inputStream ) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue( inputStream, clazz );

        } catch (IOException e) {
            throw new RuntimeException( e );
        }

    }


    private static class PrettyPrinter extends DefaultPrettyPrinter {
        public static final PrettyPrinter instance = new PrettyPrinter();

        public PrettyPrinter() {
            _arrayIndenter = Lf2SpacesIndenter.instance;
        }
    }


    private static class Factory extends JsonFactory {
        @Override
        protected JsonGenerator _createGenerator(Writer out, IOContext ctxt) throws IOException {
            return super._createGenerator(out, ctxt).setPrettyPrinter(PrettyPrinter.instance);
        }
    }

}
