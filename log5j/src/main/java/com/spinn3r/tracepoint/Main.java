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

import java.util.*;
import java.util.regex.*;

import com.spinn3r.util.*;

public class Main {

    /**
     * Parse out a log file, given on the command line, and return all
     * tracepoints.
     */
    public static void main( String[] args ) throws Exception {

        String path = args[0];

        CharSequence seq = new FileCharSequence( path );
        Pattern p = Pattern.compile( "(?m)Exception tracepoint: (.*)\n(\t.*\n)+" );
        Matcher m = p.matcher( seq );

        Map<String,String> traces = new HashMap();
        Map<String,Integer> hits  = new HashMap();

        List<String> keys = new ArrayList();
        
        while( m.find() ) {

            String key = m.group( 1 ) ;

            if ( ! traces.containsKey( key ) ) {
                
                String trace = m.group( 0 );

                keys.add( key );
                traces.put( key, trace );
                
            }

            if ( hits.containsKey( key ) ) {
                hits.put( key, hits.get( key ) + 1 );
            } else {
                hits.put( key, 1 );
            }

        }

        System.out.printf( "Found %,d tracepoints.\n", traces.size() );
        System.out.printf( "---\n" );

        for( String key : keys ) {
            System.out.printf( "%s", traces.get( key ) );
        }

    }

}