package com.spinn3r.artemis.init.terminal;

import java.io.ByteArrayOutputStream;

/**
 *
 */
public class CapturedTerminal {

    protected final ByteArrayOutputStream out = new ByteArrayOutputStream();
    protected final ByteArrayOutputStream err = new ByteArrayOutputStream();

    /**
     * Get stdout
     */
    public String getOut() {
        return out.toString();
    }


    /**
     * Get stderr.
     */
    public String getErr() {
        return err.toString();
    }

}
