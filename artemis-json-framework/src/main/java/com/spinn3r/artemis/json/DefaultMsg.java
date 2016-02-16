package com.spinn3r.artemis.json;

/**
 *
 */
public class DefaultMsg implements Msg {

    private final String json;

    public DefaultMsg(String json) {
        if ( json == null )
            throw new NullPointerException( "json is null" );
        this.json = json;

    }

    @Override
    public String toJSON() {
        return json;
    }

    @Override
    public String toString() {
        return json;
    }

}


