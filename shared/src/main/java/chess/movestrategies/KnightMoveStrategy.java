package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class KnightMoveStrategy extends MoveStrategy {
    public KnightMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        super(board, myPosition, piece);
    }

    @Override
    public void addMoves(Set<ChessMove> moves) {
        addStaticMoves(new int[][] {
                {2, -1}, {2, 1},
                {1, -2}, {1, 2},
                {-1, -2}, {-1, 2},
                {-2, -1}, {-2, 1}
        }, moves);
    }
}
