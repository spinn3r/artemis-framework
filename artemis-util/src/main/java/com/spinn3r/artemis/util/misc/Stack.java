package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class Stack {

    public static StackTraceElement caller() {

        Exception e = new Exception();

        List<StackTraceElement> frames =  Arrays.asList( e.getStackTrace() );

        // remove duplicate frames with the same class name.
        frames = removeFramesWithDuplicateClasses( frames );

        int idx = -1;

        for (int i = 0; i < frames.size(); i++) {

            StackTraceElement frame = frames.get( i );

            if ( frame.getClassName().equals( Stack.class.getName() ) ) {
                idx = i + 2;
            }


        }

        if ( idx == -1 )
            throw new RuntimeException( "Unable to determine caller" );

        for (int i = idx; i < frames.size(); i++) {

            StackTraceElement frame = frames.get( i );

            String frameClassName = frame.getClassName();

            if ( frameClassName.startsWith( "sun.reflect." ) || frameClassName.startsWith( "java.lang.reflect." ) ) {
                continue;
            }

            if ( frameClassName.startsWith( "com.google.inject.internal." ) ) {
                continue;
            }

            /*
            if ( frameClassName.startsWith( "org.junit." ) ) {
                continue;
            }

            if ( frameClassName.startsWith( "com.intellij." ) ) {
                continue;
            }
            */

            return frame;

        }

        throw new RuntimeException( "Unable to determine caller" );

    }

    private static List<StackTraceElement> removeFramesWithDuplicateClasses( List<StackTraceElement> input ) {

        Set<String> seen = Sets.newHashSet();

        List<StackTraceElement> result = Lists.newArrayList();

        for (StackTraceElement stackTraceElement : input) {

            String key = stackTraceElement.getClassName();

            if ( seen.contains( key ) )
                continue;

            seen.add( key );

            result.add( stackTraceElement );

        }

        return result;

    }

}
