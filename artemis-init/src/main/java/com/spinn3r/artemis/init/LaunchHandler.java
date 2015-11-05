package com.spinn3r.artemis.init;

/**
 * Used so that we can separate logic to init or init and start every time we
 * launch services.
 */
interface LaunchHandler {

    public void onLaunch( ServicesTool servicesTool ) throws Exception;

}
