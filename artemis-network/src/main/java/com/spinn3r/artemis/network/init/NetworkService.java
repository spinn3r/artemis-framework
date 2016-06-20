package com.spinn3r.artemis.network.init;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.builder.listener.RequestListeners;
import com.spinn3r.artemis.network.builder.proxies.PrioritizedProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyRegistry;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.artemis.network.cookies.SetCookieDescription;
import com.spinn3r.artemis.network.cookies.jar.CookieJarManager;
import com.spinn3r.artemis.network.fetcher.ContentFetcher;
import com.spinn3r.artemis.network.fetcher.DefaultContentFetcher;
import com.spinn3r.artemis.network.validators.DefaultHttpResponseValidators;
import com.spinn3r.artemis.network.validators.HttpResponseValidators;
import com.spinn3r.artemis.util.daemon.WaitForPort;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;

import java.net.*;
import java.util.List;
import java.util.Map;

/**
 * Network service which instruments requests.
 */

@Config( path = "network.conf",
         required = true,
         implementation = NetworkConfig.class )
public class NetworkService extends BaseService {

    private static final int TIMEOUT = 60000;

    private final NetworkConfig networkConfig;

    private final WaitForPort waitForPort;

    private final AtomicReferenceProvider<Proxy> proxyProvider = new AtomicReferenceProvider<>( null );

    private final AtomicReferenceProvider<ProxyReference> proxyReferenceProvider = new AtomicReferenceProvider<>( null );

    private final AtomicReferenceProvider<ProxyRegistry> proxyRegistryProvider = new AtomicReferenceProvider<>( null );

    private final AtomicReferenceProvider<RequestSettingsRegistry> requestSettingsRegistryProvider = new AtomicReferenceProvider<>( null );

    private final AtomicReferenceProvider<CookieJarManager> cookieJarManagerProvider = new AtomicReferenceProvider<>(null);

    private final AtomicReferenceProvider<ThreadLocalCookieStore> threadLocalCookieStoreProvider = new AtomicReferenceProvider<>(null);

    @Inject
    public NetworkService(NetworkConfig networkConfig, WaitForPort waitForPort) {
        this.networkConfig = networkConfig;
        this.waitForPort = waitForPort;
    }

    @Override
    public void init() {

        advertise( HttpRequestBuilder.class, ConfiguredHttpRequestBuilder.class );
        advertise( CrawlingHttpRequestBuilder.class, ConfiguredHttpRequestBuilder.class );
        advertise( DirectHttpRequestBuilder.class, DefaultDirectHttpRequestBuilder.class );
        advertise( ContentFetcher.class, DefaultContentFetcher.class );
        advertise( RequestListeners.class, new RequestListeners() );
        advertise( HttpResponseValidators.class, new DefaultHttpResponseValidators() );
        provider( Proxy.class, proxyProvider );
        provider( ProxyReference.class, proxyReferenceProvider );
        provider( ProxyRegistry.class, proxyRegistryProvider );
        provider( RequestSettingsRegistry.class, requestSettingsRegistryProvider );
        provider( CookieJarManager.class, cookieJarManagerProvider );
        provider( ThreadLocalCookieStore.class, threadLocalCookieStoreProvider );

        // *** create the default proxy

        String defaultProxy = networkConfig.getDefaultProxy();

        if ( defaultProxy != null ) {

            ProxySettings proxySettings = networkConfig.getProxies().get( defaultProxy );

            if ( proxySettings == null ) {
                throw new RuntimeException( "Default proxy has no entry in proxies." );
            }

            ProxyReference proxyReference = createPrioritizedProxyReference( defaultProxy, proxySettings );

            proxyProvider.set( proxyReference.getProxy() );
            proxyReferenceProvider.set( proxyReference );

            info( "Using default proxy: %s", proxyReference.getProxy()  );

        }

        // *** configure the proxy registry...

        List<PrioritizedProxyReference> prioritizedProxyReferences = Lists.newArrayList();

        for (Map.Entry<String, ProxySettings> entry : networkConfig.getProxies().entrySet()) {

            String name = entry.getKey();
            ProxySettings proxySettings = entry.getValue();

            prioritizedProxyReferences.add( createPrioritizedProxyReference( name, proxySettings ) );

        }

        ProxyRegistry proxyRegistry = new ProxyRegistry( prioritizedProxyReferences );
        proxyRegistryProvider.set( proxyRegistry );

        // *** now copy over settings.

        RequestSettingsRegistry requestSettingsRegistry = new RequestSettingsRegistry( networkConfig.getRequests() );
        requestSettingsRegistryProvider.set( requestSettingsRegistry );

    }

    @Override
    public void start() throws Exception {

        // test all our proxies to make sure they work.

        if ( proxyReferenceProvider.get() != null ) {
            testProxyReference( proxyReferenceProvider.get() );
        }

        for (ProxyReference proxyReference : proxyRegistryProvider.get().getPrioritizedProxyReferences()) {
            testProxyReference( proxyReference );
        }

        if ( networkConfig.isCookieManagerEnabled() ) {

            List<SetCookieDescription> setCookieDescriptions = networkConfig.getCookies();

            ThreadLocalCookieStore threadLocalCookieStore = new ThreadLocalCookieStore(setCookieDescriptions);
            threadLocalCookieStoreProvider.set(threadLocalCookieStore);
            CookieManager cookieManager = new CookieManager(threadLocalCookieStore, null);

            CookieHandler.setDefault(cookieManager);

        } else {

            cookieJarManagerProvider.set(new CookieJarManager(networkConfig.getCookieJarReferences()));

        }
    }

    private PrioritizedProxyReference createPrioritizedProxyReference(String name, ProxySettings proxySettings ) {

        String host = proxySettings.getHost();
        int port = proxySettings.getPort();

        SocketAddress addr = new InetSocketAddress( host, port );

        Proxy.Type type = Proxy.Type.HTTP;

        Proxy proxy = new Proxy( type, addr );

        return new PrioritizedProxyReference( name, proxySettings.getPriority(), proxySettings.getRegex(), host, port, proxy );

    }

    private void testProxyReference( ProxyReference proxyReference ) throws Exception {

        Preconditions.checkNotNull( proxyReference );

        String host = proxyReference.getHost();
        int port = proxyReference.getPort();

        info( "Waiting for proxy on %s:%s", host, port );

        waitForPort.waitFor( host, port, TIMEOUT );

    }

}
