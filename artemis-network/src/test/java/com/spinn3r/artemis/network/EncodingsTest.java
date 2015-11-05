package com.spinn3r.artemis.network;

import com.google.common.base.Charsets;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class EncodingsTest {

    @Test
    public void testParseMetaContentType() throws Exception {

        String content = "<head>\n" +
                            "<meta charset=\"UTF-8\">\n" +
                            "</head>";

        byte[] bytes = content.getBytes( Charsets.UTF_8 );

        assertEquals( "UTF-8", Encodings.parseMetaContentType( bytes ) );

    }

    @Test
    public void testParseMetaContentType2() throws Exception {

        String content = "<head>\n" +
                           "<meta charset=\"UTF-8\"/>\n" +
                           "</head>";

        byte[] bytes = content.getBytes( Charsets.UTF_8 );

        assertEquals( "UTF-8", Encodings.parseMetaContentType( bytes ) );

    }

    @Test
    public void testParseMetaContentType3() throws Exception {

        String content = "<head>\n" +
          "<meta charset=\"UTF-8\"/>\n" +
          "<meta name=\"blitz\" content=\"mu-05b0e8d5-cb271ed3-ccb8d146-4a748f7f\">";

        byte[] bytes = content.getBytes( Charsets.UTF_8 );

        assertEquals( "UTF-8", Encodings.parseMetaContentType( bytes ) );

    }

    @Test
    public void testParseMetaContentType4() throws Exception {

        String content = "<!DOCTYPE html>\n" +
                           "<!--[if lt IE 7]>      <html class=\"no-js lt-ie9 lt-ie8 lt-ie7\" lang=\"es-ES\" prefix=\"og: http://ogp.me/ns#\"> <![endif]-->\n" +
                           "<!--[if IE 7]>         <html class=\"no-js lt-ie9 lt-ie8\" lang=\"es-ES\" prefix=\"og: http://ogp.me/ns#\"> <![endif]-->\n" +
                           "<!--[if IE 8]>         <html class=\"no-js lt-ie9\" lang=\"es-ES\" prefix=\"og: http://ogp.me/ns#\"> <![endif]-->\n" +
                           "<!--[if gt IE 8]><!--> <html class=\"no-js\" lang=\"es-ES\" prefix=\"og: http://ogp.me/ns#\"> <!--<![endif]-->\n" +
                           "<head>\n" +
                           "<meta charset=\"UTF-8\"/>\n" +
                           "<meta name=\"blitz\" content=\"mu-05b0e8d5-cb271ed3-ccb8d146-4a748f7f\">\n" +
                           "<meta property=\"twitter:account_id\" content=\"4503599631609862\"/>\n" +
                           "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                           "<title>Quinta victoria yanqui seguida - El Diario de Yucatán</title>\n" +
                           "<title>Quinta victoria yanqui seguida - El Diario de Yucatán</title>\n";

        byte[] bytes = content.getBytes( Charsets.ISO_8859_1 );

        assertEquals( "UTF-8", Encodings.parseMetaContentType( bytes ) );


    }
}