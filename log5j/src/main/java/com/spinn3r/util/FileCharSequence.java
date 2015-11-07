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
package com.spinn3r.util;

import java.io.*;
import java.util.*;

/**
 * Char seq backed by a file.
 */
public class FileCharSequence implements CharSequence {

    private int offset = 0;
    private RandomAccessFile in = null;
    private File file = null;

    private boolean trace = false;
    
    public FileCharSequence( String path ) throws IOException {
        this( new File( path ) );
    }

    public FileCharSequence( File file ) throws IOException {
        this( file , new RandomAccessFile( file, "r" ) );
    }

    public FileCharSequence( File file, RandomAccessFile in ) throws IOException {
        this.in = in;
        this.file = file;
    }

    public void setTrace( boolean trace ) {
        this.trace = trace;
    }
    
    public char charAt(int index) {

        if ( trace )
            System.out.printf( "charAt: %,d\n", index );
        
        try {

            //NOTE: this is safe but might NOT perform very well
            in.seek( index );
            
            char c = (char)in.read();
            ++offset;
            return c;

        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        
    }

    public int length() {

        int result = (int)file.length();

        if ( trace )
            System.out.printf( "length(): %,d\n", result );

        return result;
    }
    
    public CharSequence subSequence( int start, int end ) {

        if ( trace )
            System.out.printf( "subSequence: %,d : %,d\n", start, end );

        char[] c = new char[ end - start ];

        int cidx=0;
        for( int i = start; i < end; ++i ) {
            c[cidx] = charAt( i );
            ++cidx;            
        }

        return new String( c );
        
    }

    public String toString() {

        if ( trace )
            System.out.printf( "toString\n" );

        throw new RuntimeException( "not implemented" );
    }
    
}
