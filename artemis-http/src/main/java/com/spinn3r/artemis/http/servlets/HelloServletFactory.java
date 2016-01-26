package com.spinn3r.artemis.http.servlets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.advertisements.Version;

/**
 *
 */
public class HelloServletFactory {

    private Provider<Version> versionProvider;

    @Inject
    HelloServletFactory(Provider<Version> versionProvider) {
        this.versionProvider = versionProvider;
    }

    public HelloServlet create() {
        return create( "hello world" );
    }

    public HelloServlet create( String message ) {
        return new HelloServlet( versionProvider, message  );
    }

}
