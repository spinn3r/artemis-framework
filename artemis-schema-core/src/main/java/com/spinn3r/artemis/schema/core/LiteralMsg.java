package com.spinn3r.artemis.schema.core;

import com.spinn3r.artemis.json.JSONConvertible;
import com.spinn3r.artemis.json.JSONConverter;
import com.spinn3r.artemis.json.converters.StringJSONConverter;

import static com.google.common.base.Preconditions.*;

/**
 * A literal JSON message with a json string body and an id.
 */
public class LiteralMsg implements JSONConvertible {

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

    @Override
    public JSONConverter getJSONConverter() {
        return new StringJSONConverter(json);
    }

}
