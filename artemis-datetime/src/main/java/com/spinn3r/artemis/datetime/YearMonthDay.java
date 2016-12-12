package com.spinn3r.artemis.datetime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YearMonthDay {

    private int year;

    private int month;

    private int day;

    public YearMonthDay(@JsonProperty("year") int year,
                        @JsonProperty("month") int month,
                        @JsonProperty("day") int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "YearMonthDay{" +
                 "year=" + year +
                 ", month=" + month +
                 ", day=" + day +
                 '}';
    }


}
