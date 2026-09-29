package exceptions;

public final class Exceptions {
    private Exceptions() {
    }

    /**
     * Thrown when the client is unauthorized to perform a specific request.
     */
    public static class NotAuthorizedException extends RuntimeException {
        public NotAuthorizedException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a data object already exists.
     */
    public static class AlreadyTakenException extends RuntimeException {
        public AlreadyTakenException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a data object doesn't exist.
     */
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}


