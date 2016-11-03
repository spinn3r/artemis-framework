package com.spinn3r.artemis.util.text;

/**
 *
 */
public class PrintBuffer {

    private final StringBuilder buff;

    public PrintBuffer() {
        this.buff = new StringBuilder();
    }

    public PrintBuffer(int capacity) {
        this.buff = new StringBuilder(capacity);
    }

    public PrintBuffer printf(String format, Object... args) {
        buff.append(String.format(format, args));
        return this;
    }

    public PrintBuffer println(String text) {
        buff.append(text);
        buff.append("\n");
        return this;
    }

    public PrintBuffer println() {
        buff.append("\n");
        return this;
    }

    @Override
    public String toString() {
        return buff.toString();
    }

}
