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

import java.math.*;
import java.security.*;

/**
 * MD5 cryptographic hash function:
 *
 * http://en.wikipedia.org/wiki/MD5
 *
 * MD5 should generally be deprecated but for key generation purposes where
 * performance is a factor MD5 is much faster (especially when testing).
 * 
 */
public class Hex {

    public static String encode(byte[] data) {
        return String.format( "%x", new BigInteger( 1, data ) );
    }

}