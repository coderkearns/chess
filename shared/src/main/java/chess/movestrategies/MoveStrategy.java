package chess.movestrategies;

import java.util.Set;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class MoveStrategy {

    public final ChessBoard board;
    public final ChessPosition myPosition;
    public final ChessPiece myPiece;

    public MoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        this.board = board;
        this.myPosition = myPosition;
        this.myPiece = myPiece;
    }

    public void addMoves(Set<ChessMove> possibleMoves) {
        throw new RuntimeException("Not implemented");
    };

    protected boolean isValidMoveNormally(int row, int col) {
        ChessPosition newPosition = new ChessPosition(row, col);

        // Don't allow the current space
        if (row == myPosition.getRow() && col == myPosition.getColumn())
            return false;
        // Don't allow moving out of bounds
        if (row < 1 || row > 8 || col < 1 || col > 8)
            return false;

        // Don't allow capturing own pieces
        var targetedPiece = board.getPiece(newPosition);
        if (targetedPiece != null && targetedPiece.getTeamColor() == myPiece.getTeamColor())
            return false;

        return true;
    }
}
