package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class KingMoveStrategy extends MoveStrategy {
    public KingMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        super(board, myPosition, piece);
    }

    @Override
    public void addMoves(Set<ChessMove> moves) {
        addStaticMoves(new int[][] {
                {1, -1}, {1, 0}, {1, 1},
                {0, -1}, {0, 1},
                {-1, -1}, {-1, 0}, {-1, 1}
        }, moves);
    }
}
