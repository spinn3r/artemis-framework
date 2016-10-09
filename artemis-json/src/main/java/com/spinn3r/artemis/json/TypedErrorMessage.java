package com.spinn3r.artemis.json;

/**
 * Generic JSON error message.
 */
public class TypedErrorMessage {

    private boolean failed = true;

    private String message = null;

    private Enum type = null;

    public TypedErrorMessage(boolean failed, String message, Enum type) {
        this.failed = failed;
        this.message = message;
        this.type = type;
    }

    public TypedErrorMessage(String message, Enum type) {
        this.message = message;
        this.type = type;
    }

    public TypedErrorMessage(String message) {
        this.message = message;
    }

    public boolean getFailed() {
        return failed;
    }

    public String getMessage() {
        return message;
    }

    public Enum getType() {
        return type;
    }

    public String toJSON() {
        return JSON.toJSON(this);
    }

    @Override
    public String toString() {
        return "TypedErrorMessage{" +
                 "failed=" + failed +
                 ", message='" + message + '\'' +
                 ", type=" + type +
                 '}';
    }

}
