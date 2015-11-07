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

import java.nio.charset.*;
import java.security.*;

/**
 *
 */
public class GenericMessageDigest {

    private static Charset UTF8 = Charset.forName( "UTF-8" );

    private ThreadLocalMessageDigest local = null;

    public GenericMessageDigest( String name ) {
        this.local = new ThreadLocalMessageDigest( name );
    }
    
    public byte[] encode( final String content ) {
       return encode( content.getBytes( UTF8 ) );
    }
    
    public byte[] encode( final byte[] bytes ) {

        MessageDigest md = local.get();        
        md.reset();
        return md.digest( bytes );

    }

    public MessageDigest getMessageDigest() {
        return local.get();
    }
        
}

