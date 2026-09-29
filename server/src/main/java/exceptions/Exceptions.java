package exceptions;

public final class Exceptions {
    private Exceptions() {
    }

    /**
     * Thrown when the user sends a bad request.
     */
    public static class BadRequestException extends RuntimeException {
        public BadRequestException() {
            super("bad request");
        }
    }

    /**
     * Thrown when the client is unauthorized to perform a specific request.
     */
    public static class NotAuthorizedException extends RuntimeException {
        public NotAuthorizedException() {
            super("not authorized");
        }
    }

    /**
     * Thrown when a data object already exists.
     */
    public static class AlreadyTakenException extends RuntimeException {
        public AlreadyTakenException() {
            super("already taken");
        }
    }

    /**
     * Thrown when a data object doesn't exist.
     */
    public static class NotFoundException extends RuntimeException {
        public NotFoundException() {
            super("not found");
        }
    }
}


