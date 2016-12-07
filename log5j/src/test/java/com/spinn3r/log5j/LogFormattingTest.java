package com.spinn3r.log5j;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LogFormattingTest {

    private List<LogEvent> logEvents;

    private Logger logger;

    @Before
    public void setUp() throws Exception {

        logEvents = new ArrayList<>();
        logger = new Logger("test", false, new TestInternalLogger());

    }

    @Test
    public void testInfo() throws Exception {

        logger.info("This is a test");

        Assert.assertEquals("[LogEvent{time=xxxx, threadName='main', level=INFO, formatMessage='This is a test', params=[], throwable=null}]",
                            canonicalize(logEvents.toString()));
    }

    @Test
    public void testInfo1() throws Exception {

        logger.info("This is a test: %s", "hello");

        Assert.assertEquals("[LogEvent{time=xxxx, threadName='main', level=INFO, formatMessage='This is a test: %s', params=[hello], throwable=null}]",
                            canonicalize(logEvents.toString()));
    }


    private String canonicalize(String text) {
        return text.replaceAll("time=[0-9]+", "time=xxxx");
    }

    private class TestInternalLogger implements InternalLogger {

        @Override
        public boolean isEnabled(LogLevel level) {
            return true;
        }

        @Override
        public void log(LogEvent event) {
            logEvents.add(event);
        }

    }

}
