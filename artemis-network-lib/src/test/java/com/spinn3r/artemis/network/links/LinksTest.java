package com.spinn3r.artemis.network.links;

import org.junit.Test;

import static org.junit.Assert.*;

public class LinksTest {

    @Test
    public void testIsValid() throws Exception {

        assertTrue(Links.isValid("http://conakrylemag.tumblr.com/post/140454804468/les-valeurs-suivies-à-la-clôture-de-la-bourse-de" ));
        assertTrue(Links.isValid("http://conakrylemag.tumblr.com/post/140454804468/les-valeurs-suivies-%C3%A0-la-cl%C3%B4ture-de-la-bourse-de"));
        assertTrue(Links.isValid("http://localhost:8081/1.html"));
        assertTrue(Links.isValid("http://voanews.com/flashembed.aspx?t=vid&id=3399935&w=100%&h=600&skin=embededfullscreen"));
        assertTrue(Links.isValid("http://rferl.org/flashembed.aspx?t=vid&id=27832986&w=100%&h=600&skin=embededfullscreen"));
        assertTrue(Links.isValid("http://example.com:8081/1.html"));
        assertFalse(Links.isValid("ftp://cnn.com"));

        assertFalse(Links.isValid(null));
        assertFalse(Links.isValid(""));
        assertFalse(Links.isValid("http://example.com foo.bar"));

    }

}