package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public static class Notification extends ServerMessage {
        final String message;

        public Notification(String message) {
            super(ServerMessageType.NOTIFICATION);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class Error extends ServerMessage {
        final String errorMessage;

        public Error(String message) {
            super(ServerMessageType.ERROR);
            this.errorMessage = message;
        }

        public String getMessage() {
            return errorMessage;
        }
    }

    public static class LoadGame extends ServerMessage {
        final ChessGame game;

        public LoadGame(ChessGame game) {
            super(ServerMessageType.LOAD_GAME);
            this.game = game;
        }

        public ChessGame getGame() {
            return game;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "serverMessageType=" + serverMessageType +
                '}';
    }
}
