package com.spinn3r.artemis.corpus.test.memoizer;

/**
 *
 */
public interface Transformer<T> {

    T deserialize(String content) throws Exception;

    String serialize(T obj) throws Exception;

    /**
     * Just return the input. Helpful if the output is just going to be the
     * string input.
     */
    Transformer<String> IDENTITY = new Transformer<String>() {

        @Override
        public String deserialize(String content) throws Exception {
            return content;
        }

        @Override
        public String serialize(String obj) throws Exception {
            return obj;
        }

    };

}
