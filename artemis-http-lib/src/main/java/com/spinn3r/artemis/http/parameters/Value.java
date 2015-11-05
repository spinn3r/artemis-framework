package com.spinn3r.artemis.http.parameters;

/**
 *
 */
public class Value {

    private String value = null;

    public Value(String value) {
        this.value = value;
    }

    public int asInt() {
        return asInt( 0 );
    }

    public int asInt( int _default ) {

        if( value != null )
            return Integer.parseInt( value );

        return _default;

    }

    public boolean asBoolean() {
        return asBoolean( false );
    }

    public boolean asBoolean( boolean _default ) {

        if( value != null )
            return Boolean.parseBoolean( value );

        return _default;

    }

    public String asString() {
        return asString( null );
    }

    public String asString( String _default) {

        if( value == null )
            return _default;

        return value;
    }

    public long asLong() {
        return asLong( 0 );
    }

    public long asLong( long _default ) {

        if( value != null )
            return Long.parseLong( value );

        return _default;

    }

    @Override
    public String toString() {
        return value;
    }

}
