package com.spinn3r.artemis.schema.core;

import static com.google.common.base.Preconditions.*;

/**
 * A literal JSON message with a json string body and an id.
 */
public class LiteralMsg implements Msg {

    private String id;

    private String json;

    public LiteralMsg(String id, String json) {

        checkNotNull( id );
        checkNotNull( json );

        this.id = id;
        this.json = json;

    }

    public String getId() {
        return id;
    }

    public String getJson() {
        return json;
    }

    @Override
    public String toJSON() {
        return json;
    }

    @Override
    public String toString() {
        return "LiteralMsg{" +
                 "id='" + id + '\'' +
                 ", json='" + json + '\'' +
                 '}';
    }

}
