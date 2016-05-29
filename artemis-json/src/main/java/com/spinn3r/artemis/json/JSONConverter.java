package com.spinn3r.artemis.json;

import com.spinn3r.artemis.util.text.UTF8Data;

import java.nio.ByteBuffer;

/**
 *
 */
public interface JSONConverter {

    String asString();

    byte[] asByteArray();

    ByteBuffer asByteBuffer();

    UTF8Data asUTF8Data();

}
