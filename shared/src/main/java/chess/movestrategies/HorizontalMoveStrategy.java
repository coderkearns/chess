package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class HorizontalMoveStrategy extends MoveStrategy {
    public HorizontalMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        super(board, myPosition, myPiece);
    }

    @Override
    public void addMoves(Set<ChessMove> possibleMoves) {
        // For each direction, keep moving forward until an invalid spot is found.
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Vertical up
        for (int i = row + 1; i < 9; i++) {
            if (addPosition(i, col, possibleMoves)) {
                break;
            }
        }

        // Vertical down
        for (int i = row - 1; i > 0; i--) {
            if (addPosition(i, col, possibleMoves)) {
                break;
            }
        }

        // Horizontal right
        for (int i = col + 1; i < 9; i++) {
            if (addPosition(row, i, possibleMoves)) {
                break;
            }
        }

        // Horizontal left
        for (int i = col - 1; i > 0; i--) {
            if (addPosition(row, i, possibleMoves)) {
                break;
            }
        }
    }
}
