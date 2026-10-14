package exceptions;

public final class Exceptions {
    private Exceptions() {
    }

    /**
     * Thrown when the server fails in a way the client doesn't control.
     */
    public static class ServerErrorException extends RuntimeException {
        public ServerErrorException() {
            super("internal server error");
        }

        public ServerErrorException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when the user sends a bad request.
     */
    public static class BadRequestException extends ServerErrorException {
        public BadRequestException() {
            super("bad request");
        }
    }

    /**
     * Thrown when the client is unauthorized to perform a specific request.
     */
    public static class NotAuthorizedException extends ServerErrorException {
        public NotAuthorizedException() {
            super("not authorized");
        }
    }

    /**
     * Thrown when a data object already exists.
     */
    public static class AlreadyTakenException extends ServerErrorException {
        public AlreadyTakenException() {
            super("already taken");
        }
    }

    /**
     * Thrown when a data object doesn't exist.
     */
    public static class NotFoundException extends ServerErrorException {
        public NotFoundException() {
            super("not found");
        }
    }
}


