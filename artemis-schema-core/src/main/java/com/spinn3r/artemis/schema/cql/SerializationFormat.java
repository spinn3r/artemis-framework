package com.spinn3r.artemis.schema.cql;

/**
 *
 */
public enum SerializationFormat {

    /**
     * Just write the records inline as CQL rows. Don't actually serialize them.
     */
    INLINE(1),

    /**
     * Serialize objects as JSON with UTF8 charset encoding.
     */
    JSON_UTF8(2);

    private final int value;

    SerializationFormat(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
