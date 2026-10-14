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
    }

}


