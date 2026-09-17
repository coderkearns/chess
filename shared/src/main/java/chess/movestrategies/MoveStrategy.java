package chess.movestrategies;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public abstract class MoveStrategy {
    final ChessBoard board;
    final ChessPosition myPosition;
    final ChessPiece piece;

    public MoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        this.board = board;
        this.myPosition = myPosition;
        this.piece = piece;
    }

    public abstract void addMoves(Set<ChessMove> moves);

    protected boolean isPositionValid(ChessPosition newPosition) {
        return board.isValid(newPosition) && newPosition != myPosition;
    }

    protected boolean isPositionFree(ChessPosition newPosition, boolean allowEnemy) {
        ChessPiece targetPiece = board.getPiece(newPosition);
        if (targetPiece == null) {
            return true;
        } else if (targetPiece.getTeamColor() == piece.getTeamColor()) {
            return false;
        } else {
            return allowEnemy;
        }
    }

    protected boolean isPositionFree(ChessPosition newPosition) {
        return isPositionFree(newPosition, true);
    }

    protected void addMovesInLine(int[] lineDelta, Set<ChessMove> moves) {
        for (int i = 1; true; i++) {
            ChessPosition newPosition = myPosition.delta(lineDelta[0] * i, lineDelta[1] * i);
            if (isPositionValid(newPosition) && isPositionFree(newPosition)) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }

            if (!isPositionValid(newPosition) || board.getPiece(newPosition) != null) {
                break;
            }
        }
    }

    protected void addStaticMoves(int[][] staticDeltas, Set<ChessMove> moves) {
        for (var delta : staticDeltas) {
            ChessPosition newPosition = myPosition.delta(delta[0], delta[1]);
            if (isPositionValid(newPosition) && isPositionFree(newPosition)) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }
}
