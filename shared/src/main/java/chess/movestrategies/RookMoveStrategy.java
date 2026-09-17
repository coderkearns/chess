package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class RookMoveStrategy extends MoveStrategy {
    public RookMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        super(board, myPosition, piece);
    }

    @Override
    public void addMoves(Set<ChessMove> moves) {
        addMovesInLine(new int[]{1, 0}, moves); // up
        addMovesInLine(new int[]{-1, 0}, moves); // down
        addMovesInLine(new int[]{0, -1}, moves); // left
        addMovesInLine(new int[]{0, 1}, moves); // right
    }
}
