package com.spinn3r.artemis.init.advertisements;

/**
 * The name of the daemon that is running.  Can be used for logging but also
 * to load config based on the daemon name.  This enables us to do dependency
 * injection for loading the config from /etc/$daemon
 */
public class Daemon {

    private final String value;

    public Daemon(String value) {
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
