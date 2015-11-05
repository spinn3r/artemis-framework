package com.spinn3r.artemis.init;

/**
 *
 */
public class Configs {

    public static Config readConfigAnnotation(Class<?> clazz) {

        Config config = clazz.getAnnotation( Config.class );

        if ( config != null ) {
            return config;
        }

        Class<?> parent = clazz.getSuperclass();

        if ( parent == null ) {
            return null;
        }

        return readConfigAnnotation( parent );

    }

}
