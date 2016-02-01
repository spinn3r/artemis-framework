package com.spinn3r.artemis.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 *
 */
public class JSON {

    private static final ObjectMapper OBJECT_MAPPER = createStaticObjectMapper();

    /**
     * Convert the object to JSON but also make it so that each arrays are pretty
     * printed one entry per line.
     *
     */
    public static String toJSON( Object obj ) {

        try {

            return OBJECT_MAPPER.writeValueAsString( obj );

        } catch (JsonProcessingException e) {
            throw new RuntimeException( e );
        }

    }

    public static void toJSON( Object obj, OutputStream outputStream  ) throws IOException {

        try {

            OBJECT_MAPPER.writeValue( outputStream, obj );

        } catch (JsonProcessingException e) {
            throw new RuntimeException( e );
        }

    }



    public static <T> T fromJSON( Class<T> clazz, String content ) {

        if ( content == null )
            throw new NullPointerException( "content" );

        try {

            ObjectMapper objectMapper = createObjectMapper();
            return objectMapper.readValue( content, clazz );

            /*
            ObjectMapper objectMapper = createObjectMapper();
            objectMapper.getFactory().enable( com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS );
            objectMapper.setPropertyNamingStrategy( new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy() );
            return objectMapper.readValue( content, clazz );
            */

        } catch (IOException e) {
            throw new RuntimeException( e );
        }

    }

    public static <T> T fromJSON( Class<T> clazz, InputStream inputStream ) {

        if ( inputStream == null )
            throw new NullPointerException( "content" );

        try {

            ObjectMapper objectMapper = createObjectMapper();
            return objectMapper.readValue( inputStream, clazz );

        } catch (IOException e) {
            throw new RuntimeException( e );
        }

    }

    public static <T> T fromJSON( Class<T> clazz, byte[] data ) {

        if ( data == null )
            throw new NullPointerException( "content" );

        try {

            ObjectMapper objectMapper = createObjectMapper();
            return objectMapper.readValue( data, clazz );

        } catch (IOException e) {
            throw new RuntimeException( e );
        }

    }

    public static <T> T fromJSON(Class<T> clazz, Map<String,Object> map) {

        ObjectMapper objectMapper = createObjectMapper();
        return objectMapper.convertValue( map, clazz );

    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new GuavaModule());
        return mapper;
    }

    private static ObjectMapper createStaticObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper( new Factory() );

        objectMapper.configure( SerializationFeature.INDENT_OUTPUT, true );
        objectMapper.disable( SerializationFeature.FAIL_ON_EMPTY_BEANS );

        return objectMapper;
    }

    @SuppressWarnings( { "deprecation", "serial" } )
    private static class PrettyPrinter extends DefaultPrettyPrinter {
        public static final PrettyPrinter instance = new PrettyPrinter();

        public PrettyPrinter() {
            _arrayIndenter = Lf2SpacesIndenter.instance;
        }
    }


    @SuppressWarnings( "serial" )
    private static class Factory extends JsonFactory {
        @Override
        protected JsonGenerator _createGenerator(Writer out, IOContext ctxt) throws IOException {
            return super._createGenerator(out, ctxt).setPrettyPrinter(PrettyPrinter.instance);
        }
    }

}
