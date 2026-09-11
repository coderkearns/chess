package chess.movestrategies;

import java.util.Set;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class HorizontalMoveStrategy extends MoveStrategy {
    HorizontalMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        super(board, myPosition, myPiece);
    }

    @Override
    public void addMoves(Set<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        // Horizontal left
        // Horizontal right
        // Vertical up
        // Vertical down
    }
}
