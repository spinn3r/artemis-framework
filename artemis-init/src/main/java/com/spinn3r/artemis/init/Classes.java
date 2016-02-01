package com.spinn3r.artemis.init;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 *
 */
@SuppressWarnings( "serial" )
public class Classes<T> extends LinkedHashSet<Class<? extends T>> {

    public final Class<T> base;

    public Classes(Class<T> base) {
        this.base = base;
    }

    public Classes<T> with( Class<? extends T> clazz ) {
        add( clazz ) ;
        return this;
    }

    public Classes<T> and( Class<? extends T> clazz ) {
        return with( clazz );
    }

    public Class<T> getBase() {
        return base;
    }

    public static <T> Classes<T> classes( Class<T> base ) {
        return new Classes<>( base );
    }

}
