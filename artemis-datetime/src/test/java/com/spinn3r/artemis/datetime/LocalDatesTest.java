package com.spinn3r.artemis.datetime;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LocalDatesTest {

    @Test
    public void testToYearMonthDay() throws Exception {

        assertEquals("YearMonthDay{year=1976, month=4, day=23}",
                     LocalDates.toYearMonthDay(LocalDate.of(1976, 4, 23)).toString());

    }

}