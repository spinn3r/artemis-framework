package com.spinn3r.artemis.util;

import com.spinn3r.artemis.util.rethrow.Rethrow;
import org.junit.Test;

public class RethrowTest {

    @Test
    public void testEvaluate() throws Exception {

        Rethrow.evaluate(() -> {
            throw new MyException();
        });

    }

    public static class MyException extends Exception {

        public MyException() {
        }

        public MyException(String message) {
            super(message);
        }

        public MyException(String message, Throwable cause) {
            super(message, cause);
        }

        public MyException(Throwable cause) {
            super(cause);
        }

    }

}