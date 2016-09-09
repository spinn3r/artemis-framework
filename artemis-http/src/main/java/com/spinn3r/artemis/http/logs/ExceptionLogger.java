package com.spinn3r.artemis.http.logs;

import com.spinn3r.log5j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

/**
 *
 */
public class ExceptionLogger  extends AbstractLifeCycle implements RequestLog {

    private static final Logger log = Logger.getLogger();

    @Override
    public void log(Request request, Response response) {

        Throwable throwable = (Throwable)request.getAttribute("javax.servlet.error.exception");

        if ( throwable != null) {
            log.error("Encountered exception when processing request: ", throwable);
        }

    }

}
