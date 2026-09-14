package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class DiagonalMoveStrategy extends MoveStrategy {
    public DiagonalMoveStrategy(ChessBoard board, ChessPosition myPosition,
                                ChessPiece myPiece) {
        super(board, myPosition, myPiece);
    }

    @Override
    public void addMoves(Set<ChessMove> possibleMoves) {
        // For each direction, keep moving forward until an invalid spot is found.
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Up-right
        for (int i = 1; i < 9; i++) {
            if (addPosition(row + i, col + i, possibleMoves)) {
                break;
            }
        }

        // Up-left
        for (int i = 1; i < 9; i++) {
            if (addPosition(row + i, col - i, possibleMoves)) {
                break;
            }
        }

        // Down-right
        for (int i = 1; i < 9; i++) {
            if (addPosition(row - i, col + i, possibleMoves)) {
                break;
            }
        }

        // Down-left
        for (int i = 1; i < 9; i++) {
            if (addPosition(row - i, col - i, possibleMoves)) {
                break;
            }
        }
    }
}
