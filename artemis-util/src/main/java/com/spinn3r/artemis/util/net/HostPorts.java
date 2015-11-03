package com.spinn3r.artemis.util.net;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Strings;

import java.util.List;

/**
 *
 */
public class HostPorts {

    public static List<HostPort> createHostPorts( String... addresses ) {
        return createHostPorts( Strings.toList(addresses) );
    }

    /**
     * Create a list of host ports from text addresses in host:port syntax.
     */
    public static List<HostPort> createHostPorts( List<String> addresses ) {

        List<HostPort> result = Lists.newArrayList();

        for (String address : addresses) {
            result.add( new HostPort( address ) );
        }

        return result;

    }

}
