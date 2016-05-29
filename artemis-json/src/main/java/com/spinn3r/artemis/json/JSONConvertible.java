package com.spinn3r.artemis.json;

import com.spinn3r.artemis.json.converters.DefaultJSONConverter;

/**
 * An object that can create a JSONConverter to convert it to various data types.
 */
public interface JSONConvertible extends Msg {

    default JSONConverter getJSONConverter() {
        return new DefaultJSONConverter(this);
    }

}
