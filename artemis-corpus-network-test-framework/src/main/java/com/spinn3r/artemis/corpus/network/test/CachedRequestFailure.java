package com.spinn3r.artemis.corpus.network.test;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class CachedRequestFailure {

    private String message;

    private int responseCode;

    private CachedRequestFailure() {
    }

    public CachedRequestFailure(@JsonProperty("message") String message,
                                @JsonProperty("responseCode") int responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String toString() {
        return "CachedRequestFailure{" +
                 "message='" + message + '\'' +
                 ", responseCode=" + responseCode +
                 '}';
    }

}
