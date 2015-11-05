package com.spinn3r.artemis.http;


import org.eclipse.jetty.server.RequestLog;

/**
 *
 */
public class RequestLogReference {

    private RequestLog requestLog;

    public RequestLogReference(RequestLog requestLog) {
        this.requestLog = requestLog;
    }

    public RequestLog getRequestLog() {
        return requestLog;
    }
}
