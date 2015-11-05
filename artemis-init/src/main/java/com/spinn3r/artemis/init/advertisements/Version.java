package com.spinn3r.artemis.init.advertisements;

/**
 * The version the system is running on.
 */
public class Version {

    private String value;

    public Version(String value) {
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
