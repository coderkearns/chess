package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor teamColor;
    private final ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> possibleMoves = new HashSet<>();

        // TODO continune
        if (this.pieceType == PieceType.KING) {
            addMoveHelper(1, -1, true, null, board, myPosition, possibleMoves);
            addMoveHelper(1, 0, true, null, board, myPosition, possibleMoves);
            addMoveHelper(1, 1, true, null, board, myPosition, possibleMoves);
            addMoveHelper(0, -1, true, null, board, myPosition, possibleMoves);
            addMoveHelper(0, 1, true, null, board, myPosition, possibleMoves);
            addMoveHelper(-1, -1, true, null, board, myPosition, possibleMoves);
            addMoveHelper(-1, 0, true, null, board, myPosition, possibleMoves);
            addMoveHelper(-1, 1, true, null, board, myPosition, possibleMoves);
        }

        if (this.pieceType == PieceType.ROOK || this.pieceType == PieceType.QUEEN) {

        }

        return possibleMoves;
    }

    private void addMoveHelper(int deltaRow, int deltaCol, boolean allowCapturing, ChessPiece.PieceType promotionPiece, ChessBoard board, ChessPosition myPosition, Set<ChessMove> possibleMoves) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + deltaRow, myPosition.getColumn() + deltaCol);

        // Don't allow the current space
        if (newPosition.getRow() == myPosition.getRow() && newPosition.getColumn() == myPosition.getColumn()) {
            return;
        }

        // Don't allow out-of-bounds moves
        if (newPosition.getRow() < 1 || newPosition.getRow() > 8) {
            return;
        }
        if (newPosition.getColumn() < 1 || newPosition.getColumn() > 8) {
            return;
        }

        // Don't allow capturing own pieces or standing on non-capturable pieces
        var targetedPiece = board.getPiece(newPosition);
        if (targetedPiece != null) {
            if (targetedPiece.teamColor == this.teamColor) {
                return;
            }
            if (!allowCapturing) {
                return;
            }
        }

        // TODO if a piece is in the way to this move, block it too.

        possibleMoves.add(new ChessMove(myPosition, newPosition, promotionPiece));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                teamColor +
                " " + pieceType +
                '}';
    }
}
