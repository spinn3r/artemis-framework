package com.spinn3r.artemis.sequence;

/**
 *
 */
public class SequenceGeneratorRuntimeException extends RuntimeException {

    public SequenceGeneratorRuntimeException() {
    }

    public SequenceGeneratorRuntimeException(String message) {
        super(message);
    }

    public SequenceGeneratorRuntimeException(Throwable cause) {
        super(cause);
    }

    public static class WriterIdTooSmall extends SequenceGeneratorRuntimeException {

        public WriterIdTooSmall() {
        }

        public WriterIdTooSmall(String message) {
            super(message);
        }
    }

    public static class WriterIdTooLarge extends SequenceGeneratorRuntimeException {

        public WriterIdTooLarge() {
        }

        public WriterIdTooLarge(String message) {
            super(message);
        }

    }

    public static class GlobalMutexExpired extends SequenceGeneratorRuntimeException {

        public GlobalMutexExpired(Throwable cause) {
            super(cause);
        }

    }

}
