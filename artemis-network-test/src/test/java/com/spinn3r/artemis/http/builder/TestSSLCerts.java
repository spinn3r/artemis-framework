package com.spinn3r.artemis.http.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
@Ignore
public class TestSSLCerts extends BaseLauncherTest {

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp( DirectNetworkService.class);
    }

    @Test
    public void testLetsEncryptWithCommunityRun() throws Exception {

        String content = httpRequestBuilder.get( "https://www.communityrun.org/" ).execute().getContentWithEncoding();

        System.out.printf( "%s\n", content );

    }

    public static void main(String[] args) throws Exception {

        Launcher launcher = Launcher.newBuilder().build();
        launcher.launch( new ServiceReferences().add( DirectNetworkService.class ) );
        TestSSLCerts testSSLCerts = launcher.getInjector().getInstance( TestSSLCerts.class );
        testSSLCerts.testLetsEncryptWithCommunityRun();

    }

}
