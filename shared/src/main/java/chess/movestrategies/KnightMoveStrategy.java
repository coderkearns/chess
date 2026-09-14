package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class KnightMoveStrategy extends MoveStrategy {
    public KnightMoveStrategy(ChessBoard board, ChessPosition myPosition,
                              ChessPiece myPiece) {
        super(board, myPosition, myPiece);
    }

    static final int[][] moveDeltas = {{2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}};

    @Override
    public void addMoves(Set<ChessMove> possibleMoves) {
        // For each direction, keep moving forward until an invalid spot is found.
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (var moveDelta : moveDeltas) {
            ChessPosition newPosition = new ChessPosition(row + moveDelta[0], col + moveDelta[1]);
            System.out.println(newPosition);
            if (isValidMoveNormally(newPosition)) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

    }
}
