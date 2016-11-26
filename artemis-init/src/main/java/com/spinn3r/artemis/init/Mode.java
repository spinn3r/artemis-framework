package com.spinn3r.artemis.init;

/**
 *
 */
public enum Mode {

    /**
     * No mode.  Not yet called init or launch
     */
    NONE,

    /**
     * Initializing only, not starting services.
     */
    INIT,

    /**
     * Performing init and start of all services.  This is used when we're
     * actually running a production app.
     */
    LAUNCH

}
