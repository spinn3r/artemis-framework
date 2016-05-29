package com.spinn3r.artemis.json.converters;

import com.google.common.base.Charsets;
import com.spinn3r.artemis.json.JSONConverter;
import com.spinn3r.artemis.util.text.UTF8Data;

import java.nio.ByteBuffer;

/**
 *
 */
public class StringJSONConverter implements JSONConverter {

    private final String value;

    public StringJSONConverter(String value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public byte[] asByteArray() {
        return value.getBytes(Charsets.UTF_8);
    }

    @Override
    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(asByteArray());
    }

    @Override
    public UTF8Data asUTF8Data() {
        return new UTF8Data(asByteBuffer());
    }

}
