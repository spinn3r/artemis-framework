package com.spinn3r.artemis.network;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class TestThreadLocalCookieStoreConcurrently extends BaseLauncherTest {

    @Inject
    HttpRequestExecutorFactory httpRequestExecutorFactory;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp(SyntheticClockService.class,
                DirectNetworkService.class);
    }

    @Test
    public void testThreadLocalCookieStore() throws Exception {

        AtomicBoolean testResult1 = new AtomicBoolean(Boolean.TRUE);
        AtomicBoolean testResult2 = new AtomicBoolean(Boolean.TRUE);

        testCookieStore(testResult1, testResult2);

        assertTrue(testResult1.get());
        assertTrue(testResult2.get());
    }

    @Test
    @Ignore // Make the test simpler with executors just one thread sets the cookie and the other thread asserts the cookie is not there
    public void testSharedInMemoryCookieStore() throws Exception {

        DefaultHttpRequestBuilder.C_HANDLER = new CookieManager(); // InMemoryCookieStore
        CookieHandler.setDefault(DefaultHttpRequestBuilder.C_HANDLER);

        final AtomicBoolean testResult1 = new AtomicBoolean(Boolean.TRUE);
        final AtomicBoolean testResult2 = new AtomicBoolean(Boolean.TRUE);

        testCookieStore(testResult1, testResult2);

        assertTrue(testResult1.get());
        assertFalse(testResult2.get());
    }


    private void testCookieStore(AtomicBoolean testResult1, AtomicBoolean testResult2) throws InterruptedException {

        String cookie1 = "name=test1";
        String resource1 = "http://httpbin.org/cookies/set?"+ cookie1;
        String cookie2 = "name=test2";
        String resource2 = "http://httpbin.org/cookies/set?"+ cookie2;

        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread1 = new TestThread(resource1,cookie1, testResult1, countDownLatch, false);
        thread1.setName("T1_THREAD");
        thread1.start();
        Thread thread2 = new TestThread(resource2,cookie2, testResult2, countDownLatch, true);
        thread2.setName("T2_THREAD");
        thread2.start();

        countDownLatch.await();
    }

    private class TestThread extends Thread {

        private final String resource;
        private final String cookie;
        private final AtomicBoolean testResult;
        private final CountDownLatch countDownLatch;
        private boolean waitLast;

        public TestThread(String resource, String cookie, AtomicBoolean testResult, CountDownLatch countDownLatch, boolean waitLast) {
            this.resource = resource;
            this.cookie = cookie;
            this.testResult = testResult;
            this.countDownLatch = countDownLatch;
            this.waitLast = waitLast;
        }

        @Override
        public void run() {
            try {
                if(waitLast){
                    while(countDownLatch.getCount() > 1){
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {}
                    }
                }
                HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

                HttpRequest httpRequest = null;
                httpRequest = httpRequestExecutor.execute(() -> httpRequestBuilder.get(resource).execute().connect());
                assertEquals(200, httpRequest.getResponseCode());
                assertEquals(0, httpRequestExecutor.getRetries());
                URI uri = new URI(resource);
                CookieHandler aDefault = CookieHandler.getDefault();
                Map<String, List<String>> actual = aDefault.get(uri, new HashMap<>());

                List<String> cookies = actual.get("Cookie");
                System.out.println(cookies);

                testResult.set(cookies.iterator().next().equals(cookie));

            } catch (IOException | URISyntaxException e) {
                testResult.set(false);
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
