package com.spinn3r.artemis.init.advertisements;

/**
 * An advertisement for the role of a given machine.
 */
public class Role {

    private final String value;

    public Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}
