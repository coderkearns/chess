package websocket.messages;

import chess.ChessBoard;
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

    public static class LoadGame extends ServerMessage {
        public LoadGame() {
            super(ServerMessageType.LOAD_GAME);
        }
    }

    public static class Error extends ServerMessage {
        final String msg;

        public Error(String msg) {
            super(ServerMessageType.ERROR);
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }

    public static class Notification extends ServerMessage {
        final ChessGame.TeamColor winner;
        final ChessGame.TeamColor currentTurn;
        final ChessBoard board;

        public Notification(ChessGame.TeamColor winner, ChessGame.TeamColor currentTurn, ChessBoard board) {
            super(ServerMessageType.NOTIFICATION);
            this.winner = winner;
            this.currentTurn = currentTurn;
            this.board = board;
        }

        public ChessGame.TeamColor getWinner() {
            return winner;
        }

        public ChessGame.TeamColor getCurrentTurn() {
            return currentTurn;
        }

        public ChessBoard getBoard() {
            return board;
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
