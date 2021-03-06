package com.spinn3r.artemis.init;

import com.spinn3r.artemis.time.Started;

/**
 * Advertisement which allows us to read the status of the system.
 *
 * The cycle goes from STOPPED, then STARTING, STARTED, STOPPING, then
 * back to STOPPED.
 *
 */
public enum Lifecycle {

    /**
     * Idle state waiting for you to start the system.  Not yet initialized.
     * This is the beginning state.
     */
    IDLE,
    INITIALIZING,
    INITIALIZED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED;

    /**
     * Return true if we are "running".  Either started or starting.
     *
     */
    public boolean isRunning() {
        return this == STARTED || this == STARTING;
    }

}
