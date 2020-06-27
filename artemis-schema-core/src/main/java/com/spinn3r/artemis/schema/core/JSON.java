package com.spinn3r.artemis.schema.core;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Various helper functions for working with JSON.
 */
public class JSON {

    public static void writeStringLongMap( JsonGenerator generator, String name, Map<String,Long> map ) throws IOException {

        // we write JSON sparse ...
        if ( map.size() == 0 )
            return;

        generator.writeFieldName( name );

        generator.writeStartObject();

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            generator.writeObjectField( entry.getKey(), entry.getValue() );
        }

        generator.writeEndObject();

    }

    public static void writeStringMap( JsonGenerator generator, String name, Map<String,?> map ) throws IOException {

        // we write JSON sparse ...
        if ( map.size() == 0 )
            return;

        generator.writeFieldName( name );

        generator.writeStartObject();

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            generator.writeObjectField( entry.getKey(), entry.getValue() );
        }

        generator.writeEndObject();

    }

    public static void writeStringToMultiStringMap(JsonGenerator generator, String name, Map<String,List<String>> map ) throws IOException {

        // we write JSON sparse ...
        if ( map.size() == 0 )
            return;

        generator.writeFieldName( name );

        generator.writeStartObject();

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {

            generator.writeFieldName( entry.getKey() );

            generator.writeStartArray();

            for (String value : entry.getValue()) {
                generator.writeString( value );
            }

            generator.writeEndArray();

        }

        generator.writeEndObject();

    }


    public static void writeLongMap( JsonGenerator generator, String name, Map<Long,Double> map ) throws IOException {

        // we write JSON sparse ...
        if ( map.size() == 0 )
            return;

        generator.writeFieldName( name );

        generator.writeStartObject();

        for (Map.Entry<Long, ?> entry : map.entrySet()) {
            generator.writeObjectField( entry.getKey().toString(), entry.getValue() );
        }

        generator.writeEndObject();

    }


    public static void writeStringSet( JsonGenerator generator, String name, Collection<String> set ) throws IOException {

        // we write JSON sparse ...
        if ( set.size() == 0 )
            return;

        generator.writeFieldName( name );

        generator.writeStartArray();

        for ( String member : set ) {
            generator.writeString( member );
        }

        generator.writeEndArray();

    }

    public static void writeIntegerSet( JsonGenerator generator, String name, Collection<Integer> set ) throws IOException {

        // we write JSON sparse ...
        if ( set.size() == 0 )
            return;

        generator.writeFieldName( name );

        generator.writeStartArray();

        for ( int member : set ) {
            generator.writeNumber( member );
        }

        generator.writeEndArray();

    }

    public static void writeLongSet( JsonGenerator generator, String name, Collection<Long> set ) throws IOException {

        // we write JSON sparse ...
        if ( set.size() == 0 )
            return;

        generator.writeFieldName( name );

        generator.writeStartArray();

        for ( long member : set ) {
            generator.writeNumber( member );
        }

        generator.writeEndArray();

    }

    @Deprecated
    public static <T> void writeMapSet(JsonGenerator generator, String name, Collection<T> set) throws IOException  {

        writeSet(generator, name, set);
    }

    public static <T> void writeSet(JsonGenerator generator, String name, Collection<T> set) throws IOException  {
        if ( set.size() == 0 )
            return;

        generator.writeFieldName( name );

        generator.writeStartArray();

        for ( T member : set ) {
            generator.writeObject( member );
        }

        generator.writeEndArray();
    }
}
