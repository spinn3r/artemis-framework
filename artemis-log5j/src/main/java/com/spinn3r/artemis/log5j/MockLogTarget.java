package com.spinn3r.artemis.log5j;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 */
public class MockLogTarget implements LogTarget {

    public List<String> messages = Lists.newArrayList();

    @Override
    public void warn(String format, Object... args) {
        messages.add( String.format( format, args ) );
    }

}
