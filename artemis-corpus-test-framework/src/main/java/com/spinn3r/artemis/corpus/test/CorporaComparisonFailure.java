package com.spinn3r.artemis.corpus.test;

import org.junit.ComparisonFailure;

/**
 * A comparison failure that has a message that's a unified diff for easy use
 * in logs but still supports getExpected and getActual for use in tools.
 */
public class CorporaComparisonFailure extends ComparisonFailure {

    private String message;

    public CorporaComparisonFailure(String message, String expected, String actual) {
        super( message, expected, actual );
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
