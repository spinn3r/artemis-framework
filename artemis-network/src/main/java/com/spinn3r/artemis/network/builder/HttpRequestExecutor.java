package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.URLResourceRequest;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.log5j.Logger;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;

/**
 * Execute HTTP request but wrap them in retry logic.
 */
public class HttpRequestExecutor {

    private static final Logger log = Logger.getLogger();

    private final Clock clock;

    private int maxRetries = 5;

    private int sleepIntervalMillis = 30_000;

    private int retries = 0;

    HttpRequestExecutor(Clock clock, NetworkConfig networkConfig) {
        this.clock = clock;

        this.maxRetries = networkConfig.getExecutor().getMaxRetries();
        this.sleepIntervalMillis = networkConfig.getExecutor().getSleepIntervalMillis();
    }

    /**
     *
     * @throws NetworkException When the maximum number of retries have been
     * exhausted.
     */
    public HttpRequest execute( NetworkRequester networkRequester ) throws NetworkException {

        NetworkException cause = null;

        for (int retryIter = 0; retryIter <= maxRetries; retryIter++) {

            try {

                if ( retryIter > 0 ) {
                    clock.sleepUninterruptibly( sleepIntervalMillis, TimeUnit.MILLISECONDS );
                    ++retries;
                }

                HttpRequest result = networkRequester.execute();

                checkNotNull( result, "result" );

                return result;

            } catch (NetworkException e) {

                cause = e;

                if ( isTransientHttpException( e ) ) {
                    log.info( "HTTP request failed (sleeping for %,d ms): %s", sleepIntervalMillis, e.getMessage() );
                } else {
                    break;
                }

            }

        }

        throw cause;

    }

    private boolean isTransientHttpException(NetworkException e) {

        if ( e.getResponseCode() == HttpRequest.STATUS_CONNECT_TIMEOUT )
            return true;

        if ( e.getResponseCode() == HttpRequest.STATUS_READ_TIMEOUT )
            return true;

        return e.getResponseCode() >= 500 && e.getResponseCode() <= 599;

    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getSleepIntervalMillis() {
        return sleepIntervalMillis;
    }

    public void setSleepIntervalMillis(int sleepIntervalMillis) {
        this.sleepIntervalMillis = sleepIntervalMillis;
    }

    public int getRetries() {
        return retries;
    }
}
