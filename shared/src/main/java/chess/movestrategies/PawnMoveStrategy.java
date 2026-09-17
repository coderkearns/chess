package chess.movestrategies;

import chess.*;

import java.util.Set;

public class PawnMoveStrategy extends MoveStrategy {
    final int initialRow;
    final int promotionRow;
    final int rowDelta;

    public PawnMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        super(board, myPosition, piece);
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            initialRow = 2;
            promotionRow = 8;
            rowDelta = 1;
        } else {
            initialRow = 7;
            promotionRow = 1;
            rowDelta = -1;
        }
    }

    private void addPossiblePromotionMove(ChessPosition newPosition, Set<ChessMove> moves) {
        if (newPosition.getRow() == promotionRow) {
            moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
    }

    @Override
    public void addMoves(Set<ChessMove> moves) {
        // normal forward when empty
        ChessPosition forwardPosition = myPosition.delta(rowDelta, 0);
        if (isPositionValid(forwardPosition) && isPositionFree(forwardPosition, false)) {
            addPossiblePromotionMove(forwardPosition, moves);

            // double forward when empty on initial row
            if (myPosition.getRow() == initialRow) {
                ChessPosition doubleForwardPosition = myPosition.delta(rowDelta * 2, 0);
                if (isPositionValid(doubleForwardPosition) && isPositionFree(doubleForwardPosition, false)) {
                    addPossiblePromotionMove(doubleForwardPosition, moves);
                }
            }
        }

        // attack diagonally
        for (int colDelta : new int[] {-1, 1}) {
            ChessPosition diagonalPosition = myPosition.delta(rowDelta, colDelta);
            if (isPositionValid(diagonalPosition) && board.getPiece(diagonalPosition) != null && board.getPiece(diagonalPosition).getTeamColor() != piece.getTeamColor()) {
                addPossiblePromotionMove(diagonalPosition, moves);
            }
        }
    }
}
