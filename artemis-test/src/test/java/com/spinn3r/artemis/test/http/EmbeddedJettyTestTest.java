package com.spinn3r.artemis.test.http;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class EmbeddedJettyTestTest extends EmbeddedJettyTest {

    public EmbeddedJettyTestTest() {
        super( createResponseGeneratorMap() );
    }

    Launcher launcher;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Override
    @Before
    @SuppressWarnings( "unchecked" )
    public void setUp() throws Exception {
        super.setUp();

        launcher = Launcher.newBuilder().build();
        launcher.launch( DirectNetworkService.class );

        launcher.getInjector().injectMembers( this );
    }

    @Test
    public void test1() throws Exception {

        String link =  String.format( "http://localhost:%s/test1.html", PORT );

        String content = httpRequestBuilder.get( link ).execute().getContentWithEncoding();

        System.out.printf( "content: %s\n", content);

        assertEquals( "this is test1", content );

    }

    public static DefaultResponseManager createResponseGeneratorMap() {

        DefaultResponseManager result = new DefaultResponseManager();

        result.path( "/test1.html", new DefaultResponseGenerator()
                                      .setResponse( "this is test1" ) );

        return result;

    }

}
