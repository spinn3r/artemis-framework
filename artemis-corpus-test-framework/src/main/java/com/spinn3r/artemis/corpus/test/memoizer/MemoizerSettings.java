package com.spinn3r.artemis.corpus.test.memoizer;

/**
 *
 */
public interface MemoizerSettings {

    boolean isUpdateMode(Class<?> parent);

    String failureMessage(Class<?> parent);

    /**
     * Read settings from system properties.
     */
    MemoizerSettings SYSTEM_PROPERTIES = new MemoizerSettings() {

        @Override
        public boolean isUpdateMode(Class<?> parent) {
            return "true".equals(System.getProperty(createPropertyName(parent)));
        }

        @Override
        public String failureMessage(Class<?> parent) {
            return String.format("Use -D%s=true to force update", createPropertyName(parent));
        }

        private String createPropertyName(Class<?> parent) {
            return String.format("%s:update-enabled", parent.getName());
        }

    };

    class Configured implements MemoizerSettings {

        private final boolean updateMode;

        public Configured(boolean updateMode) {
            this.updateMode = updateMode;
        }

        @Override
        public boolean isUpdateMode(Class<?> parent) {
            return updateMode;
        }

        @Override
        public String failureMessage(Class<?> parent) {
            return "Configured as not enabled";
        }

    }

}
