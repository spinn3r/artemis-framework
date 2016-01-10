package com.spinn3r.artemis.network.init;

import com.spinn3r.artemis.init.BaseService;

/**
 * Increases the value of the TCP send/receive buffers
 */
public class StandardSocketOptionsService extends BaseService {


    // TODO: I'm not sure the best way to go forward here. It would be
    // ideal if we would have some standard socket sizes for applications
    // that are local and then large socket options for remote apps running
    // in circle CI, etc.
    //
    // in theory then I could giave a -Dsocket-options-profile=large command
    // line argument or override this in the config file.
    //
    // The value could just be a set of profiles that we can use for
    // various socket options


}
