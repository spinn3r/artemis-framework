package com.spinn3r.artemis.util;

/**
 *
 */
public class HTMLString {

    private final String data;

    public HTMLString(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }

    public String toHTML() {
        return data;
    }

}
