package com.spinn3r.artemis.fluent;

import com.spinn3r.artemis.util.primitives.liberal.LiberalIntParser;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A Str is a fluent API to String.
 */
public class Str {

    private String value;

    public Str() {
        this.value = null;
    }

    public Str(String value) {
        this.value = value;
    }

    public Str(Object obj) {

        if ( obj != null ) {
            this.value = obj.toString();
        } else {
            this.value = null;
        }

    }

    public boolean empty() {

        return value == null || "".equals( value );

    }

    public boolean isInt() {

        return value != null && value.matches( "-?[0-9]+" );

    }

    public Str toLowerCase() {

        if ( value == null )
            return Tuples.str( null );

        return new Str( value.toLowerCase() );

    }

    public Str unescape() {

        if ( value == null )
            return Tuples.str( null );

        return Tuples.str( StringEscapeUtils.unescapeHtml4( value ) );

    }

    public Str trim() {

        if ( value == null )
            return Tuples.str( null );

        return Tuples.str( value.trim() );

    }

    /**
     * Consume a non-empty string.
     */
    public Str consume(Consumer<String> elementQueryFunction) {

        if ( value != null && ! value.trim().equals( "" ) ) {
            elementQueryFunction.accept( value.trim() );
        }

        return this;

    }

    /**
     * Filter the current string if it does not contain the given substring.
     *
     */
    public Str find(String substr ) {

        if ( value == null )
            return Tuples.str( null );

        if ( value.contains( substr ) ) {
            return Tuples.str( value );
        }

        return Tuples.str( null );

    }

    public boolean contains( String text ) {

        if ( value == null )
            return false;

        return value.contains( text );

    }

    public int asInt() {
        return asInt( 0 );
    }

    public int asInt( int _default ) {

        if ( value == null )
            return _default;

        try {

            return LiberalIntParser.parse( value );

        } catch ( NumberFormatException e ) {
            return _default;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof Str )) return false;

        Str that = (Str) o;

        if (value != null ? !value.equals( that.value ) : that.value != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }


    public String toString( String _default ) {

        if ( value == null ) {
            return _default;
        } else {
            return value;
        }

    }

    public Optional<String> toOptional() {
        return Optional.ofNullable( this.value );
    }

    @Override
    public String toString() {
        return value;
    }

}
