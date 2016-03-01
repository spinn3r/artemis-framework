package com.spinn3r.artemis.time;

import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ISO8601DateParserTest {


    @Test
    public void testPartial() throws Exception {



        //assertEquals( "",
        //              ISO8601DateParser.parse( "2014-10-11" ) );

    }


    @Test
    @Ignore
    public void testParseWithJava8() throws Exception {

//        String strDate = "2015-08-04";
//        LocalDate aLD = LocalDate.parse(strDate);
//        System.out.println("Date: " + aLD);

        String strDatewithTime = "2016-02-27 05:28:16 -0500";
        LocalDateTime aLDT = LocalDateTime.parse(strDatewithTime);
        System.out.println("Date with Time: " + aLDT);

    }

}