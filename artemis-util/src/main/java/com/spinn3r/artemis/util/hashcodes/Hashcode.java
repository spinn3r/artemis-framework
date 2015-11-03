package com.spinn3r.artemis.util.hashcodes;

import com.google.common.base.Preconditions;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a Hashcode object as a value / type.  This makes for more readable
 * usage when used in a Map.  So instead of Map<String,Foo> we can use Map<Hashcode,Foo>
 */
public class Hashcode implements Comparable<Hashcode> {

    private final String value;

    public Hashcode(String value) {
        checkNotNull( value, "value" );
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof Hashcode )) return false;

        Hashcode hashcode = (Hashcode) o;

        if (!value.equals( hashcode.value )) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Hashcode o) {
        return value.compareTo( o.value );
    }

    @Override
    public String toString() {
        return value;
    }

}
