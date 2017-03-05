package com.spinn3r.artemis.network.cookies.jar;

import com.spinn3r.artemis.util.misc.Strings;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class FileBackedCookieJarTest {

    @Test
    public void testJSON() throws Exception {

        InputStream inputStream = Strings.toInputStream("[\n" +
                                                          "{ \"datr\": \"0006VyKvCiyyHCA-Yp4QYEaU\"},\n" +
                                                          "{ \"datr\": \"0006V-zU4DfKbRFRV9-2SeJW\"}]");

        FileBackedCookieJar fileBackedCookieJar = new FileBackedCookieJar(inputStream, CookieJarReference.Format.JSON);

        assertEquals(2, fileBackedCookieJar.backing.size());

        assertEquals("[{datr=0006VyKvCiyyHCA-Yp4QYEaU}, {datr=0006V-zU4DfKbRFRV9-2SeJW}]",
                     fileBackedCookieJar.backing.toString());

    }

    @Test
    public void testJSONS() throws Exception {

        InputStream inputStream = Strings.toInputStream("{\"SUB\":\"_2AkMv5XJNf8NhqwJRmP4Qz2nqbI13yQnEieKZuYOWJRMxHRl-yT83qmcltRCXJicG8xMNLC1QB430VSCkiXq0Lw..\",\"SUBP\":\"0033WrSXqPxfM72-Ws9jqgMF55529P9D9WF8OEV_dXP1QkhVfEV.7As9\"}\n" +
                                                          "{\"SUB\":\"_2AkMv5XJNf8NhqwJRmP4Qz2nqbI11wgvEieKZuYOWJRMxHRl-yT83qlUotRAoU-Xb9Fmuvb3HShIAzF5kKkRvWw..\",\"SUBP\":\"0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5PaJ7sK4YBFSSZeBP.5A8V\"}");

        FileBackedCookieJar fileBackedCookieJar = new FileBackedCookieJar(inputStream, CookieJarReference.Format.JSONS);

        assertEquals(2, fileBackedCookieJar.backing.size());
        assertEquals("[{SUB=_2AkMv5XJNf8NhqwJRmP4Qz2nqbI13yQnEieKZuYOWJRMxHRl-yT83qmcltRCXJicG8xMNLC1QB430VSCkiXq0Lw.., SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WF8OEV_dXP1QkhVfEV.7As9}, {SUB=_2AkMv5XJNf8NhqwJRmP4Qz2nqbI11wgvEieKZuYOWJRMxHRl-yT83qlUotRAoU-Xb9Fmuvb3HShIAzF5kKkRvWw.., SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5PaJ7sK4YBFSSZeBP.5A8V}]",
                     fileBackedCookieJar.backing.toString());

    }

}
