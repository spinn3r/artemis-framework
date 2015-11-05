package com.spinn3r.artemis.client.watcher;

/**
 * Listen for new content to be published by Spinn3r and picked up by the fetcher.
 */
public interface FetcherListener {

    /**
     * Called when new content is found, allowing you to import it into your
     * application.
     *
     * @param parse The result of parsing the content.
     */
    void onContent( Parse parse );

}
