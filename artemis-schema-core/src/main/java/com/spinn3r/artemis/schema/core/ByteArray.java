package com.spinn3r.artemis.schema.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

/**
 */
@JsonIgnoreProperties(ignoreUnknown=true)

public class ByteArray {

    protected int hashCode;

    protected byte[] data;

    public ByteArray() {
    }

    public ByteArray(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.hashCode = Arrays.hashCode( data );
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {

        if ( ! (obj instanceof ByteArray) )
            return false;

        ByteArray ba = (ByteArray)obj;

        return Arrays.equals( data, ba.data );

    }

    /**
     * Format this ByteArray as a string.
     *
     * @return
     */
    @Override
    public String toString() {
        return byteArrayToHex( data );
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

}
