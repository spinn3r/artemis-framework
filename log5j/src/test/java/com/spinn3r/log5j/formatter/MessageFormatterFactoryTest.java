package com.spinn3r.log5j.formatter;

import com.spinn3r.log5j.Log;
import com.spinn3r.log5j.LogFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: Denis Tarima
 * Date: Jun 4, 2010
 */
public class MessageFormatterFactoryTest {
    static String __lastFormat;

    static Object[] __lastArgs;

    public static class TstMessageFormatterFactory implements MessageFormatterFactory {
        public MessageFormatter create() {
            return new MessageFormatter() {
                public String format(String format, Object... args) {
                    __lastFormat = format;
                    __lastArgs = args;
                    return format;
                }
            };
        }
    }

    public static void main(String[] args) throws Exception {
        // customFormatter

        System.setProperty("log5j.formatter.factory",
                TstMessageFormatterFactory.class.getName());

        Log log = LogFactory.getLog(false);

        String format = "some message";
        int param = 5;

        log.t(new Exception("test"), format, param);

        assertEquals(format, __lastFormat);
        assertEquals(1, __lastArgs.length);
        assertEquals(param, __lastArgs[0]);

        System.exit(0);
    }

    @Test
    public void customLocale() throws Exception {
        System.setProperty("log5j.formatter.default.language", "fr");
        assertEquals("2,7", new DefaultMessageFormatterFactory().create()
                .format("%3.1f", Math.E));

        System.setProperty("log5j.formatter.default.language", "en");
        assertEquals("2.7", new DefaultMessageFormatterFactory().create()
                .format("%3.1f", Math.E));
    }
}
