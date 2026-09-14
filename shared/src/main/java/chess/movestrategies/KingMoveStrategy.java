package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class KingMoveStrategy extends StaticMoveStrategy {
    public KingMoveStrategy(ChessBoard board, ChessPosition myPosition,
                            ChessPiece myPiece) {
        super(board, myPosition, myPiece);
    }

    static final int[][] moveDeltas = {{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};

    @Override
    public void addMoves(Set<ChessMove> possibleMoves) {
        addMovesFromDeltas(possibleMoves, moveDeltas);
    }

}
