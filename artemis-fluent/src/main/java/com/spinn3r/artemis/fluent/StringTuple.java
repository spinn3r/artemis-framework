package com.spinn3r.artemis.fluent;

import java.util.Collection;

/**
 * A tuple of strings with added functions for working with strings.
 */
public class StringTuple extends Tuple<String> {

    public StringTuple(String... elements) {
        super( elements );
    }

    public StringTuple(Collection<String> elements) {
        super( elements );
    }

    public StringTuple( Tuple<String> tuple ) {
        this( tuple.backing );
    }

    public StringTuple withoutEmpty() {
        return new StringTuple( filter( (val) -> val != null && ! "".equals( val.trim() ) ) );
    }

}
