package com.spinn3r.artemis.json;

/**
 *
 */
public class JSONData {

    private final String data;

    public JSONData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }

    public String toJSON() {
        return data;
    }

}
