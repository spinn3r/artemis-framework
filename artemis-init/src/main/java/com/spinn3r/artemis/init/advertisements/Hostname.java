package com.spinn3r.artemis.init.advertisements;

/**
 * Allows us to easily advertise the hostname this machine provides.
 */
public class Hostname {

    private final String value;

    public Hostname(String value) {
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
