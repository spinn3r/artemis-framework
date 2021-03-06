package com.spinn3r.artemis.network.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.builder.cookies.StandardCookieStore;
import com.spinn3r.artemis.network.builder.cookies.ThreadLocalCookieStore;
import com.spinn3r.artemis.network.cookies.SetCookieDescription;
import com.spinn3r.artemis.network.cookies.jar.CookieJarManager;
import com.spinn3r.artemis.network.cookies.jar.CookieJarManagerFactory;
import com.spinn3r.artemis.network.fetcher.ContentFetcher;
import com.spinn3r.artemis.network.fetcher.DefaultContentFetcher;
import com.spinn3r.artemis.network.validators.DefaultHttpResponseValidators;
import com.spinn3r.artemis.network.validators.HttpResponseValidators;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;

/**
 * Provides network bindings but without proxy support and forces the
 * stock HttpRequestBuilder to go direct without a proxy backend for crawling.
 *
 * BE CAREFUL WHEN USING THIS.
 */

@Config( path = "network.conf",
         required = false,
         implementation = NetworkConfig.class )
public class DirectNetworkService extends BaseService {

    private final AtomicReferenceProvider<CookieJarManager> cookieJarManagerProvider = new AtomicReferenceProvider<>(null);

    private final AtomicReferenceProvider<ThreadLocalCookieStore> threadLocalCookieStoreProvider = new AtomicReferenceProvider<>(null);

    private final NetworkConfig networkConfig;

    private final CookieJarManagerFactory cookieJarManagerFactory;

    @Inject
    DirectNetworkService(NetworkConfig networkConfig, CookieJarManagerFactory cookieJarManagerFactory) {
        this.networkConfig = networkConfig;
        this.cookieJarManagerFactory = cookieJarManagerFactory;
    }

    @Override
    public void init() {

        advertise( HttpRequestBuilder.class, DefaultDirectHttpRequestBuilder.class );
        advertise( CrawlingHttpRequestBuilder.class, DefaultDirectHttpRequestBuilder.class );
        advertise( DirectHttpRequestBuilder.class, DefaultDirectHttpRequestBuilder.class );
        advertise( ContentFetcher.class, DefaultContentFetcher.class );
        advertise( HttpResponseValidators.class, new DefaultHttpResponseValidators() );
        provider( CookieJarManager.class, cookieJarManagerProvider );
        provider( ThreadLocalCookieStore.class, threadLocalCookieStoreProvider );

        NetworkSupport.disablePlatformLogger();

    }

    @Override
    public void start() throws Exception {

        // TODO: I don't think the SetCookieDescription stuff is working anymore.
        List<SetCookieDescription> setCookieDescriptions = networkConfig.getCookies();

        ThreadLocalCookieStore threadLocalCookieStore = new ThreadLocalCookieStore(setCookieDescriptions);
        threadLocalCookieStoreProvider.set(threadLocalCookieStore);

        if ( networkConfig.isCookieManagerEnabled() ) {
            CookieManager cookieManager = new CookieManager(new StandardCookieStore(threadLocalCookieStore), null);
            CookieHandler.setDefault(cookieManager);
        }

        cookieJarManagerProvider.set(cookieJarManagerFactory.create(networkConfig.getCookieJarReferences()));

    }

}

