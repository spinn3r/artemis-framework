package com.spinn3r.artemis.json;

/**
 * A JSON pair is a simple name value pair used for encoding streams of name
 * value pairs in \n delimited files.  This format is optimized for minimal
 * storage.
 */
public class JSONPair<K,V> {

    private final K k;

    private final V v;

    public JSONPair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public V getV() {
        return v;
    }

    @Override
    public String toString() {
        return "JSONPair{" +
                 "k=" + k +
                 ", v=" + v +
                 '}';
    }

}
