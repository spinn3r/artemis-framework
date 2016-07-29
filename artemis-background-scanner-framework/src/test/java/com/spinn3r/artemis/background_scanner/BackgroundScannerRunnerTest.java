package com.spinn3r.artemis.background_scanner;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.logging.init.ConsoleLoggingService;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BackgroundScannerRunnerTest extends BaseLauncherTest {

    @Inject
    BackgroundScannerRunnerFactory backgroundScannerRunnerFactory;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp(SyntheticClockService.class,
                    MockHostnameService.class,
                    MockVersionService.class,
                    ConsoleLoggingService.class);
    }

    @Test
    public void testWithSuccessfulBackgroundScanner() throws Exception {

        SuccessfulBackgroundScanner backgroundScanner = new SuccessfulBackgroundScanner();
        BackgroundScannerRunner backgroundScannerRunner =
          backgroundScannerRunnerFactory.create(backgroundScanner, new BackgroundScannerOptions());

        backgroundScanner.countDownLatch.await();

        backgroundScannerRunner.shutdown();

    }

    @Test
    public void testWithFailingBackgroundScanner() throws Exception {

        FailingBackgroundScanner backgroundScanner = new FailingBackgroundScanner();

        BackgroundScannerRunner backgroundScannerRunner =
          backgroundScannerRunnerFactory.create(backgroundScanner, new BackgroundScannerOptions());

        backgroundScanner.countDownLatch.await();

        await().until(() -> {
            assertThat(backgroundScanner.scans.get(), greaterThanOrEqualTo(2));
        } );

        backgroundScannerRunner.shutdown();

    }

    @Test
    public void testWithBlockingBackgroundScanner() throws Exception {

        BlockingBackgroundScanner backgroundScanner = new BlockingBackgroundScanner();

        BackgroundScannerRunner backgroundScannerRunner =
          backgroundScannerRunnerFactory.create(backgroundScanner, new BackgroundScannerOptions());

        backgroundScanner.countDownLatch.await();

        backgroundScannerRunner.shutdown();

    }

    private static class SuccessfulBackgroundScanner implements BackgroundScanner {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        @Override
        public void scan() throws BackgroundScannerException {
            countDownLatch.countDown();
        }

    }

    private static class FailingBackgroundScanner implements BackgroundScanner {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        AtomicInteger scans = new AtomicInteger();

        @Override
        public void scan() throws BackgroundScannerException {
            countDownLatch.countDown();
            scans.getAndIncrement();
            throw new RuntimeException("Failed");
        }

    }

    private static class BlockingBackgroundScanner implements BackgroundScanner {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        @Override
        public void scan() throws BackgroundScannerException {
            try {
                countDownLatch.countDown();
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                throw new BackgroundScannerException("Could not scan: ", e);
            }

        }
    }

}