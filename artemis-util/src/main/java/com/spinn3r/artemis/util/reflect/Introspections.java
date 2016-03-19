package com.spinn3r.artemis.util.reflect;

import java.lang.reflect.Field;

/**
 *
 */
public class Introspections {


    public static void introspectViaReflection(Object obj) {

        Class clazz = obj.getClass();

        while( true ) {

            if ( clazz == null )
                break;

            introspectViaReflection( obj, clazz );

            clazz = clazz.getSuperclass();

        }

    }

    public static void introspectViaReflection(Object obj, Class clazz) {

        Field[] fields;

        System.out.printf( "============== declared fields in %s\n", clazz.getName() );

        fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[ i ];
            System.out.printf( "  declared field: %s\n", field );
        }

        System.out.printf( "============== fields in %s\n", clazz.getName() );

        fields = clazz.getFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[ i ];
            System.out.printf( "  field: %s\n", field );
        }

    }

}
