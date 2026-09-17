package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class BishopMoveStrategy extends MoveStrategy {
    public BishopMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        super(board, myPosition, piece);
    }

    @Override
    public void addMoves(Set<ChessMove> moves) {
        addMovesInLine(new int[]{1, 1}, moves); // up-right
        addMovesInLine(new int[]{1, -1}, moves); // up-left
        addMovesInLine(new int[]{-1, 1}, moves); // down-right
        addMovesInLine(new int[]{-1, -1}, moves); // down-left
    }
}
