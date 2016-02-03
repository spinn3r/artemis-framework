package com.spinn3r.artemis.init.modular;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class BackingMapFacade<T> {

    private BackingMap<T> backingMap = new BackingMap<>();

    public Set<Map.Entry<Class<T>, Class<? extends T>>> entrySet() {
        //return backingMap.entrySet();
        return null;
    }

    public void put( Class<T> key, Class<? extends T> value ) {
        backingMap.put( key, value );
    }

}
