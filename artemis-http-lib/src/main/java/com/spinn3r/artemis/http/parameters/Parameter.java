package com.spinn3r.artemis.http.parameters;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 */
public class Parameter {

    private String name;
    private List<String> values;

    public Parameter(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    public Value first() {

        if ( values == null || values.size() == 0 ) {
            return new Value( null );
        }

        return new Value( values.get( 0 ) );

    }

    public List<String> values() {

        if ( values == null ) {
            return Lists.newArrayList();
        }

        return values;

    }

}
