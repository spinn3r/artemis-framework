package com.spinn3r.artemis.corpus.test.memoizer;

import com.spinn3r.artemis.corpus.test.CorporaCache;
import com.spinn3r.artemis.util.crypto.SHA1;
import com.spinn3r.artemis.util.misc.Base64;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * A memoizer takes a function, and only executes it if necessary.  We return the
 * cached result if we have it on disk already.
 */
public class Memoizer<T> {

    protected final Class<?> parent;

    protected final String basedir;

    protected final CorporaCache corporaCache;

    private final Transformer<T> transformer;

    private final MemoizerSettings memoizerSettings;

    private Memoizer(Class<?> parent, String basedir, Transformer<T> transformer, MemoizerSettings memoizerSettings, String extension) {
        this.parent = parent;
        this.basedir = basedir;
        this.transformer = transformer;
        this.memoizerSettings = memoizerSettings;
        this.corporaCache = new CorporaCache(parent, basedir, extension);
    }

    /**
     * Call the given function but only if we don't have a given key in the cache.
     *
     * This is used if the given function is slow so that we can either keep a
     * more efficient copy or reduce the amount of data or other resources being
     * used.
     *
     * @param name A unique name for the item. This can be a URL or other human
     *             readable resource BUT it must be unique.  We use this name to
     *             derive a primary key from the input.
     *
     *
     */
    public T memoize(String name, Callable<T> callable) throws Exception {

        String key = computeKey(name);

        if(corporaCache.contains(key)) {
            return transformer.deserialize(corporaCache.read(key));
        }

        if(memoizerSettings.isUpdateMode(parent)) {

            T result = callable.call();
            corporaCache.write(key, transformer.serialize(result));

            return result;

        } else {
            throw new IOException(String.format("Entry is not in the cache: %s (key=%s) (%s)", name, key, memoizerSettings.failureMessage(parent)));
        }

    }

    private String computeKey(String name) {
        return Base64.encode(SHA1.encode(name));
    }

    public static class Builder<T> {

        private final Class<?> parent;

        private final String basedir;

        private final Transformer<T> transformer;

        private String extension = "dat";

        private MemoizerSettings memoizerSettings = MemoizerSettings.SYSTEM_PROPERTIES;

        public Builder(Class<?> parent, String basedir, Transformer<T> transformer) {
            this.parent = parent;
            this.basedir = basedir;
            this.transformer = transformer;
        }

        public Builder<T> setMemoizerSettings(MemoizerSettings memoizerSettings) {
            this.memoizerSettings = memoizerSettings;
            return this;
        }

        public Builder<T> setExtension(String extension) {
            this.extension = extension;
            return this;
        }

        public Memoizer<T> build() {
            return new Memoizer<>(parent, basedir, transformer, memoizerSettings, extension);
        }

    }

}
