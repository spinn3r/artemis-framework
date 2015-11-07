package com.spinn3r.log5j;

import org.apache.log4j.BasicConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * User: Denis Tarima
 * Date: Apr 5, 2010
 */
public class LogSinkTest {
    private PrintStream _oldOut;
    private PrintStream _oldErr;

    @Before
    public void changeStdStreams() {
        _oldOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        _oldErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
    }

    @After
    public void restoreStdStreams() {
        System.setOut(_oldOut);
        System.setErr(_oldErr);
    }

    @Test
    public void logger() throws Exception {
        BasicConfigurator.configure();

        System.setProperty(LogManager.FACTORY_CLASS_NAME, "aa");

        Logger logger = Logger.getLogger(false);
        Logger logger2 = Logger.getLogger("log2", false);
        Thread.sleep(100);

        logger.debug("test %s", LogSinkTest.class.getSimpleName());

        logger.fatal("test %s", new Exception(), LogSinkTest.class.getSimpleName());

        LogSink sink = new LogSink(logger, logger2);
        sink.warn("test (%s)", new Exception(), "he");
        Thread.sleep(100);

        Log log = LogFactory.getLog();

        log.f("hmm %d", 15);

        log.w(new Exception(), "hmm %d", 15);
        Thread.sleep(10);
    }
}
