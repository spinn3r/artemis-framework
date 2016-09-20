package com.spinn3r.artemis.util.text;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class TextTableFormatterTest {

    @Test
    public void testBasicTableLayout() throws Exception {

        TextTableFormatter.TextTableFormatter3 textTableFormatter
          = TextTableFormatter.forHeadings("cpu", "memory", "disk");

        textTableFormatter.row("100%", "2GB", "100MB/s");

        assertEquals("     cpu    memory   disk      \n" +
                       "     ---    ------   ----      \n" +
                       "1.   100%   2GB      100MB/s   \n", textTableFormatter.format());

    }

    @Test
    public void testObjectFormatting() throws Exception {
        Object val = 10000;
        assertEquals("10,000", String.format("%,d", val));
    }

    @Test
    public void testColumnFormatting() throws Exception {

        TextTableFormatter.TextTableFormatter1 textTableFormatter
          = TextTableFormatter.forHeadings("foo");

        for (int i = 0; i < 110; i++) {
            textTableFormatter.row(String.format("asdf:%s", i));
        }

        assertEquals("       foo        \n" +
                       "       ---        \n" +
                       "1.     asdf:0     \n" +
                       "2.     asdf:1     \n" +
                       "3.     asdf:2     \n" +
                       "4.     asdf:3     \n" +
                       "5.     asdf:4     \n" +
                       "6.     asdf:5     \n" +
                       "7.     asdf:6     \n" +
                       "8.     asdf:7     \n" +
                       "9.     asdf:8     \n" +
                       "10.    asdf:9     \n" +
                       "11.    asdf:10    \n" +
                       "12.    asdf:11    \n" +
                       "13.    asdf:12    \n" +
                       "14.    asdf:13    \n" +
                       "15.    asdf:14    \n" +
                       "16.    asdf:15    \n" +
                       "17.    asdf:16    \n" +
                       "18.    asdf:17    \n" +
                       "19.    asdf:18    \n" +
                       "20.    asdf:19    \n" +
                       "21.    asdf:20    \n" +
                       "22.    asdf:21    \n" +
                       "23.    asdf:22    \n" +
                       "24.    asdf:23    \n" +
                       "25.    asdf:24    \n" +
                       "26.    asdf:25    \n" +
                       "27.    asdf:26    \n" +
                       "28.    asdf:27    \n" +
                       "29.    asdf:28    \n" +
                       "30.    asdf:29    \n" +
                       "31.    asdf:30    \n" +
                       "32.    asdf:31    \n" +
                       "33.    asdf:32    \n" +
                       "34.    asdf:33    \n" +
                       "35.    asdf:34    \n" +
                       "36.    asdf:35    \n" +
                       "37.    asdf:36    \n" +
                       "38.    asdf:37    \n" +
                       "39.    asdf:38    \n" +
                       "40.    asdf:39    \n" +
                       "41.    asdf:40    \n" +
                       "42.    asdf:41    \n" +
                       "43.    asdf:42    \n" +
                       "44.    asdf:43    \n" +
                       "45.    asdf:44    \n" +
                       "46.    asdf:45    \n" +
                       "47.    asdf:46    \n" +
                       "48.    asdf:47    \n" +
                       "49.    asdf:48    \n" +
                       "50.    asdf:49    \n" +
                       "51.    asdf:50    \n" +
                       "52.    asdf:51    \n" +
                       "53.    asdf:52    \n" +
                       "54.    asdf:53    \n" +
                       "55.    asdf:54    \n" +
                       "56.    asdf:55    \n" +
                       "57.    asdf:56    \n" +
                       "58.    asdf:57    \n" +
                       "59.    asdf:58    \n" +
                       "60.    asdf:59    \n" +
                       "61.    asdf:60    \n" +
                       "62.    asdf:61    \n" +
                       "63.    asdf:62    \n" +
                       "64.    asdf:63    \n" +
                       "65.    asdf:64    \n" +
                       "66.    asdf:65    \n" +
                       "67.    asdf:66    \n" +
                       "68.    asdf:67    \n" +
                       "69.    asdf:68    \n" +
                       "70.    asdf:69    \n" +
                       "71.    asdf:70    \n" +
                       "72.    asdf:71    \n" +
                       "73.    asdf:72    \n" +
                       "74.    asdf:73    \n" +
                       "75.    asdf:74    \n" +
                       "76.    asdf:75    \n" +
                       "77.    asdf:76    \n" +
                       "78.    asdf:77    \n" +
                       "79.    asdf:78    \n" +
                       "80.    asdf:79    \n" +
                       "81.    asdf:80    \n" +
                       "82.    asdf:81    \n" +
                       "83.    asdf:82    \n" +
                       "84.    asdf:83    \n" +
                       "85.    asdf:84    \n" +
                       "86.    asdf:85    \n" +
                       "87.    asdf:86    \n" +
                       "88.    asdf:87    \n" +
                       "89.    asdf:88    \n" +
                       "90.    asdf:89    \n" +
                       "91.    asdf:90    \n" +
                       "92.    asdf:91    \n" +
                       "93.    asdf:92    \n" +
                       "94.    asdf:93    \n" +
                       "95.    asdf:94    \n" +
                       "96.    asdf:95    \n" +
                       "97.    asdf:96    \n" +
                       "98.    asdf:97    \n" +
                       "99.    asdf:98    \n" +
                       "100.   asdf:99    \n" +
                       "101.   asdf:100   \n" +
                       "102.   asdf:101   \n" +
                       "103.   asdf:102   \n" +
                       "104.   asdf:103   \n" +
                       "105.   asdf:104   \n" +
                       "106.   asdf:105   \n" +
                       "107.   asdf:106   \n" +
                       "108.   asdf:107   \n" +
                       "109.   asdf:108   \n" +
                       "110.   asdf:109   \n",
                     textTableFormatter.format());

    }


    @Test
    public void testLeftJustify() throws Exception {

        TextTableFormatter.TextTableFormatter1 textTableFormatter
          = TextTableFormatter.forHeadings("foo");

        String text = TextTableFormatter.Justifier.left("hello", 10);

        assertEquals("hello     ", text);

    }


    @Test
    public void testRightJustify() throws Exception {

        TextTableFormatter.TextTableFormatter1 textTableFormatter
          = TextTableFormatter.forHeadings("foo");

        String text = TextTableFormatter.Justifier.right("hello", 10);

        assertEquals("     hello", text);

    }

}