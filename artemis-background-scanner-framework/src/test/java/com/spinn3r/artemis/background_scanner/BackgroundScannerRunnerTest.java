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
    public void testBackgroundScanner() throws Exception {

        MyBackgroundScanner backgroundScanner = new MyBackgroundScanner();
        BackgroundScannerRunner backgroundScannerRunner =
          backgroundScannerRunnerFactory.create(backgroundScanner, new BackgroundScannerOptions());

        backgroundScanner.countDownLatch.await();

        backgroundScannerRunner.shutdown();

    }

    static class MyBackgroundScanner implements BackgroundScanner {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        @Override
        public void scan() throws BackgroundScannerException {
            countDownLatch.countDown();
        }

    }

}