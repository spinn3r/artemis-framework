package com.spinn3r.artemis.time;

import java.util.Date;

/**
 * A date parsed from a stream of text/html which may be partial.
 */
public class ParsedDate {

    private Date date;

    private boolean partial = false;

    public ParsedDate(Date date) {
        this.date = date;
    }

    public ParsedDate(Date date, boolean partial) {
        this.date = date;
        this.partial = partial;
    }

    public Date getDate() {
        return date;
    }

    public boolean isPartial() {
        return partial;
    }

    @Override
    public String toString() {
        return "ParsedDate{" +
                 "date=" + date +
                 ", partial=" + partial +
                 '}';
    }

}
