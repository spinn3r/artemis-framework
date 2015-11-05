package com.spinn3r.artemis.http;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Used so that we can register our own RequestLogs to watch HTTP requests
 * as they enter/exit Jetty.
 */
public class RequestLogReferences extends CopyOnWriteArrayList<RequestLogReference> {


}
