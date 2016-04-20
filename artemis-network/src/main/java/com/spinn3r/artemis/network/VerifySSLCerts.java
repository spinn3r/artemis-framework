package com.spinn3r.artemis.network;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;

/**
 *
 */
public class VerifySSLCerts {

    private final HttpRequestBuilder httpRequestBuilder;

    @Inject
    VerifySSLCerts(HttpRequestBuilder httpRequestBuilder) {
        this.httpRequestBuilder = httpRequestBuilder;
    }

    public void testLetsEncryptWithCommunityRun() throws Exception {

        String content = httpRequestBuilder.get( "https://www.communityrun.org/" ).execute().getContentWithEncoding();

        System.out.printf( "%s\n", content );

    }

    public static void main(String[] args) throws Exception {

        Launcher launcher = Launcher.forResourceConfigLoader().build();
        launcher.launch( new ServiceReferences().add( DirectNetworkService.class ) );
        VerifySSLCerts verifySSLCerts = launcher.getInjector().getInstance( VerifySSLCerts.class );
        verifySSLCerts.testLetsEncryptWithCommunityRun();

    }

}
