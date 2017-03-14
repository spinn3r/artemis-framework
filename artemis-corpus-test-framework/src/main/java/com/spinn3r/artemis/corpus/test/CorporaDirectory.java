package com.spinn3r.artemis.corpus.test;

/**
 *
 */
public interface CorporaDirectory {

    // make this static so it can't be changed at runtime. It should only be
    // changed on startup or we could get weird behavior.
    String ROOT = System.getProperty( "corpora-cache.root", "src/test/resources/" );

    String getRoot();

    /**
     * Read settings from system properties.
     */
    CorporaDirectory SYSTEM_PROPERTIES = new CorporaDirectory() {

        @Override
        public String getRoot() {
            return ROOT;
        }

    };

    class Configured implements CorporaDirectory {

        private final String root;

        public Configured(String root) {
            this.root = root;
        }

        @Override
        public String getRoot() {
            return root;
        }

    }

}
