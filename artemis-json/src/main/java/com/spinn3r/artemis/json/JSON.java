package com.spinn3r.artemis.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 *
 */
public class JSON {

    private static final ObjectMapper INDENTED_OBJECT_MAPPER = createStaticObjectMapper(true, true);
    private static final ObjectMapper NON_INDENTED_OBJECT_MAPPER = createStaticObjectMapper(false, false);

    /**
     * Convert the object to JSON but also make it so that each arrays are pretty
     * printed one entry per line.
     */
    public static String toJSON( Object obj ) {

        try {
            return INDENTED_OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException( e );
        }

    }

    /**
     * Same as toJSON but makes the record on one line.
     */
    public static String toJSONRecord( Object obj ) {

        try {
            return NON_INDENTED_OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException( e );
        }

    }

    public static byte[] toByteArray( Object obj ) {

        try {
            return INDENTED_OBJECT_MAPPER.writeValueAsBytes(obj );
        } catch (JsonProcessingException e) {
            throw new RuntimeException( e );
        }

    }

    public static ByteBuffer toByteBuffer(Object obj ) {
        return ByteBuffer.wrap(toByteArray(obj));
    }

    public static void toJSON( Object obj, OutputStream outputStream  ) throws IOException {

        try {

            INDENTED_OBJECT_MAPPER.writeValue(outputStream, obj );

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

    public static <T> T fromJSON( Class<T> clazz, File file ) throws IOException {

        Preconditions.checkNotNull(file);

        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            return JSON.fromJSON(clazz, fileInputStream);
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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    private static ObjectMapper createStaticObjectMapper(boolean pretty, boolean indentOutput) {
        ObjectMapper objectMapper = new ObjectMapper( new CustomJSONFactory(pretty) );
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new GuavaModule());
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.configure( SerializationFeature.INDENT_OUTPUT, indentOutput );
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
    private static class CustomJSONFactory extends JsonFactory {

        private final boolean pretty;

        public CustomJSONFactory(boolean pretty) {
            super();
            this.pretty = pretty;
        }

        @Override
        protected JsonGenerator _createGenerator(Writer out, IOContext ctxt) throws IOException {

            JsonGenerator jsonGenerator = super._createGenerator(out, ctxt);

            if( pretty ) {
                return jsonGenerator.setPrettyPrinter(PrettyPrinter.instance);
            }

            return jsonGenerator;

        }

    }

}
