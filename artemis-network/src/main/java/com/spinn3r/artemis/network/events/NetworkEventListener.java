package com.spinn3r.artemis.network.events;

/**
 * Listen to network requests.
 */
public interface NetworkEventListener {

    /**
     * Called when a request is received on the network.
     *
     * @param requestReceived
     */
    public void onReceived( RequestReceived requestReceived );

}
