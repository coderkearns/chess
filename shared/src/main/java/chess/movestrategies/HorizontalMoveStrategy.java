package chess.movestrategies;

import java.util.Set;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class HorizontalMoveStrategy extends MoveStrategy {
    public HorizontalMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        super(board, myPosition, myPiece);
    }

    @Override
    public void addMoves(Set<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Vertical up
        for (int i = col + 1; i < 9; i++) {
            ChessPosition newPosition = new ChessPosition(row, i);
            if (isValidMoveNormally(newPosition)) {
                possibleMoves.add(new ChessMove(myPosition, myPosition, null));
            }
            // Stop when we hit a piece
            if (board.getPiece(newPosition) != null)
                break;
        }

        // Vertical down
        for (int i = col - 1; i > 0; i--) {
            ChessPosition newPosition = new ChessPosition(row, i);
            if (isValidMoveNormally(newPosition)) {
                possibleMoves.add(new ChessMove(myPosition, myPosition, null));
            }
            // Stop when we hit a piece
            if (board.getPiece(newPosition) != null)
                break;
        }

        // Horizontal right
        for (int i = row + 1; i < 9; i++) {
            ChessPosition newPosition = new ChessPosition(i, col);
            if (isValidMoveNormally(newPosition)) {
                possibleMoves.add(new ChessMove(myPosition, myPosition, null));
            }
            // Stop when we hit a piece
            if (board.getPiece(newPosition) != null)
                break;
        }

        // Horizontal left
        for (int i = row - 1; i > 0; i--) {
            ChessPosition newPosition = new ChessPosition(i, col);
            if (isValidMoveNormally(newPosition)) {
                possibleMoves.add(new ChessMove(myPosition, myPosition, null));
            }
            // Stop when we hit a piece
            if (board.getPiece(newPosition) != null)
                break;
        }
    }
}
