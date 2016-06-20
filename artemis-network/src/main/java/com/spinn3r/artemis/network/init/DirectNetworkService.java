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

    private final AtomicReferenceProvider<CookieJarManager> cookieJarManagerProvider = new AtomicReferenceProvider<>(null );

    private final AtomicReferenceProvider<ThreadLocalCookieStore> threadLocalCookieStoreProvider = new AtomicReferenceProvider<>(null);

    private final NetworkConfig networkConfig;

    @Inject
    DirectNetworkService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
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

    }

    @Override
    public void start() throws Exception {

        if ( networkConfig.isCookieManagerEnabled() ) {

            List<SetCookieDescription> setCookieDescriptions = networkConfig.getCookies();

            ThreadLocalCookieStore threadLocalCookieStore = new ThreadLocalCookieStore(setCookieDescriptions);
            threadLocalCookieStoreProvider.set(threadLocalCookieStore);
            CookieManager cookieManager = new CookieManager(new StandardCookieStore(threadLocalCookieStore), null);

            CookieHandler.setDefault(cookieManager);

        } else {

            cookieJarManagerProvider.set(new CookieJarManager(networkConfig.getCookieJarReferences()));

        }
    }
}
