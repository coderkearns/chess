package chess.movestrategies;

import chess.*;

import java.util.Set;

public class PawnMoveStrategy extends MoveStrategy {
    private int rowDelta;
    private int initialRow;
    private int promotionRow;

    public PawnMoveStrategy(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        super(board, myPosition, myPiece);
        if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            rowDelta = 1;
            initialRow = 2;
            promotionRow = 8;
        } else {
            rowDelta = -1;
            initialRow = 7;
            promotionRow = 1;
        }
    }

    private void addPossiblePromotionMove(ChessPosition newPosition, Set<ChessMove> possibleMoves) {
        if (newPosition.getRow() == promotionRow) {
            possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
            possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
            possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
            possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        } else {
            possibleMoves.add(new ChessMove(myPosition, newPosition, null));
        }
    }

    @Override
    public void addMoves(Set<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Forward normal move
        ChessPosition forwardPosition = new ChessPosition(row + rowDelta, col);
        if (isValidMoveNormally(forwardPosition) && board.getPiece(forwardPosition) == null) {
            addPossiblePromotionMove(forwardPosition, possibleMoves);
        }

        // Forward initial move
        if (row == initialRow) {
            ChessPosition doubleForwardPosition = new ChessPosition(row + rowDelta + rowDelta, col);
            if (isValidMoveNormally(doubleForwardPosition) && board.getPiece(forwardPosition) == null && board.getPiece(doubleForwardPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, doubleForwardPosition, null));
            }
        }

        // Diagonal attacks
        for (int colDelta : new int[]{1, -1}) {
            ChessPosition diagonalPosition = new ChessPosition(row + rowDelta, col - colDelta);
            if (isValidMoveNormally(diagonalPosition) && board.getPiece(diagonalPosition) != null) {
                addPossiblePromotionMove(diagonalPosition, possibleMoves);
            }
        }
    }
}
