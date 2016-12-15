package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.URLResourceRequest;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.log5j.Logger;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.spinn3r.artemis.network.builder.HttpRequest.*;

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
                    log.warn( "HTTP request failed.  Going to retry. (sleepIntervalMillis=%,d, retryIter=%,d, resource=%s): %s",
                              e, sleepIntervalMillis, retryIter, e.getResource(), e.getMessage() );
                } else {
                    break;
                }

            }

        }

        log.warn("Throwing non transient exception: ", cause);
        throw cause;

    }

    static boolean isTransientHttpException(Throwable e) {

        if (e instanceof NetworkException) {

            int responseCode = ((NetworkException) e).getResponseCode();

            if (responseCode == STATUS_CONNECT_TIMEOUT)
                return true;

            if (responseCode == STATUS_READ_TIMEOUT)
                return true;

            if (responseCode >= 500 && responseCode <= 599)
                return true;

        } else if ( e instanceof SSLException ||
                    e instanceof SocketException ||
                    e instanceof SocketTimeoutException) {

            // we through hard about whether socket exceptions, timeouts, etc
            // should be retried.  these are essentially identical to HTTP
            // 5xx and so I think they should be treated as such.

            return true;

        } else if (e instanceof IOException && e.getMessage().startsWith("Unable to tunnel through proxy")) {

            // Handle in an issue where the proxy itself is having problems.
            //
            // This can happen if haproxy is being restarted, if all the servers
            // are loaded too high, temporary internet connectivity to the
            // deployed proxies, etc.

            // The problem in detecting this, is that we have to use a string
            // since the JVM doesn't give us a typed exception.  This can be
            // mitigated in the future when we move away from java.net.URL
            // and start using Netty.

            //  Caused by: java.io.IOException: Unable to tunnel through proxy. Proxy returns "HTTP/1.0 503 Service Unavailable"
            // at sun.net.www.protocol.http.HttpURLConnection.doTunneling(HttpURLConnection.java:2084)
            // at sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection.connect(AbstractDelegateHttpsURLConnection.java:183)
            // at sun.net.www.protocol.https.HttpsURLConnectionImpl.connect(HttpsURLConnectionImpl.java:153)
            // at com.spinn3r.artemis.network.URLResourceRequest.initConnection1(URLResourceRequest.java:420)
            // ... 22 more

            return true;

        }

        // recurse into the causes of this exception too.
        return e.getCause() != null && isTransientHttpException(e.getCause());

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
