package com.spinn3r.artemis.json;

import com.spinn3r.artemis.json.JSON;

/**
 * Generic JSON error message.
 */
public class ErrorMessage {

    private boolean failed = true;

    private String message = null;

    public ErrorMessage(boolean failed, String message) {
        this.failed = failed;
        this.message = message;
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public boolean getFailed() {
        return failed;
    }

    public String getMessage() {
        return message;
    }

    public String toJSON() {
        return JSON.toJSON(this);
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                 "failed=" + failed +
                 ", message='" + message + '\'' +
                 '}';
    }
}
