package com.spinn3r.artemis.util.text;

import com.google.common.collect.Lists;
import junit.framework.TestCase;

import java.util.List;

public class TableFormatterTest extends TestCase {

    public void test1() throws Exception {

        List<List<String>> tmp = Lists.newArrayList();

        tmp.add( Lists.newArrayList( "xxx", "xxxxxxxxxxxxxx") );
        tmp.add( Lists.newArrayList( "xxxxxxxxxxx", "xx") );


        assertEquals( "xxx         xxxxxxxxxxxxxx \n" +
                        "xxxxxxxxxxx xx             \n",
                      TableFormatter.format( tmp ) );


    }
}