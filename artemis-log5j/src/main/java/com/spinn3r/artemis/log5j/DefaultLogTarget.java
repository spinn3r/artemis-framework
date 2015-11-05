package com.spinn3r.artemis.log5j;

import com.spinn3r.log5j.Logger;

/**
 *
 */
public class DefaultLogTarget implements LogTarget {

    private Logger target;

    public DefaultLogTarget(Logger target) {
        this.target = target;
    }

    @Override
    public void warn(String format, Object... args) {
        target.warn( format, args );
    }

}
