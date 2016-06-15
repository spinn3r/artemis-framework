/*
 * Copyright 2012 "Tailrank, Inc (Spinn3r)"
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.spinn3r.tracepoint;

import com.spinn3r.util.Hex;
import com.spinn3r.util.MD5;

/**
 * <p>
 * A Tracepoint is a stacktrace which we manually emit to the a log file along
 * with a hashcode for debug purposes.  This allows us to trace every unique
 * invocation of a method (or every unique exception).
 *
 * <p>
 * This can be valuable for tracking down resource leaks in your application by
 * emitting a trace everytime something is allocated.
 * 
 * <p> A tracepoint also has an optional set of arguments which are NOT included
 * for computing the hashcode (and only logging).  
 */
public class Tracepoint {

    StringBuffer buff = new StringBuffer();

    public Tracepoint( Object... args ) {

        init( Thread.currentThread().getStackTrace(), null, args );

    }

    public Tracepoint( Throwable t , Object... args ) {

        init( t.getStackTrace(), t, args );
    }
    
    private void init( StackTraceElement[] frames, Throwable throwable, Object... args ) {

        int offset = 2;

        if ( throwable != null ) {
            offset = 0;
        }

        for( int i = offset; i < frames.length; ++i ) {

            StackTraceElement frame = frames[i];

            buff.append( "\t" );
            buff.append( frame.toString() );
            buff.append( "\n" );
            
        }

        String stacktrace = buff.toString();

        buff = new StringBuffer();

        String tp = Hex.encode( MD5.encode( stacktrace ) );
        
        buff.append( String.format( "Exception tracepoint: %s", tp ) );

        if ( args.length != 0 )
            buff.append( " (" );

        for( int i = 0; i < args.length; i=i + 2 ) {

            if ( i != 0 )
                buff.append( ", " );
            
            buff.append( String.format( "%s = %s", args[i], args[i + 1] ) );
        }

        if ( args.length != 0 )
            buff.append( ")" );

        buff.append( "\n" );

        if ( throwable != null ) {
            
            if ( throwable.getMessage() != null ) {
                buff.append( String.format( "%s:%s\n", throwable.getClass().getName(), throwable.getMessage() ) );
            } else {
                buff.append( String.format( "%s\n", throwable.getClass().getName() ) );
            }

        }
        
        buff.append( stacktrace );

    }

    public String toString() {
        return buff.toString();
    }

}

