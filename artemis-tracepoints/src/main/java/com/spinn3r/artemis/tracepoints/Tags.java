package com.spinn3r.artemis.tracepoints;

import com.spinn3r.artemis.util.collections.ImmutableDictionary;

import java.util.Map;

/**
 *
 */
public class Tags extends ImmutableDictionary<String,String> {

    public Tags() {
    }

    public Tags(Map<String, String> parent) {
        super(parent);
    }

}
