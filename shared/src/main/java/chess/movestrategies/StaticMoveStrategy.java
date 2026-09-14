package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class StaticMoveStrategy extends MoveStrategy {
    public StaticMoveStrategy(ChessBoard board, ChessPosition myPosition,
                              ChessPiece myPiece) {
        super(board, myPosition, myPiece);
    }
    
    protected void addMovesFromDeltas(Set<ChessMove> possibleMoves, int[][] moveDeltas) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (var moveDelta : moveDeltas) {
            ChessPosition newPosition = new ChessPosition(row + moveDelta[0], col + moveDelta[1]);
            if (isValidMoveNormally(newPosition)) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }
}
