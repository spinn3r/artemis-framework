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

import java.security.*;

/**
 * ThreadLocal for the current MessageDigest.  Note that the SUN digest
 * factory is SLOW and this class will be 4x slower when using this
 * directly.  Also note that it's not threadsafe and will corrupt itself it
 * not threadlocal
 *
 * @author <a href="mailto:burton@tailrank.com">Kevin Burton</a>
 * @version $Id: ThreadLocalMessageDigest.java,v 1.2 2004/05/21 22:21:32 burton Exp $
 */
public class ThreadLocalMessageDigest extends ThreadLocal<MessageDigest> {

    private String name = null;
    
    public ThreadLocalMessageDigest( String name ) {
        this.name = name;
    }

    protected MessageDigest initialValue() {

        try {
            
            return MessageDigest.getInstance( name );
            
        } catch ( Exception e ) {
        	throw new RuntimeException( e );
        }

    }
            
}
