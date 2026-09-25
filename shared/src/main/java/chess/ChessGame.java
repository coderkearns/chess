package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        var safeMoves = new HashSet<ChessMove>();
        var moves = piece.pieceMoves(board, startPosition);

        for (var move : moves) {
            ChessBoard newBoard = board.cloneWithMove(move);
            if (!isInCheck(newBoard, piece.getTeamColor(), newBoard.findFirstPositionOf(piece.getTeamColor(), ChessPiece.PieceType.KING))) {
                safeMoves.add(move);
            }
        }

        return safeMoves;
    }

    private void toggleTeamTurn() {
        teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece == null) {
            throw new InvalidMoveException("No piece to move");
        }

        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It's not their turn");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        setBoard(board.cloneWithMove(move));
        toggleTeamTurn();
    }

    private boolean isInCheck(ChessBoard currentBoard, TeamColor teamColor, ChessPosition targetPosition) {
        for (var position : currentBoard) {
            ChessPiece piece = currentBoard.getPiece(position);
            if (piece.getTeamColor() != teamColor) {
                for (var move : piece.pieceMoves(currentBoard, position)) {
                    if (move.getEndPosition().equals(targetPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(board, teamColor, board.findFirstPositionOf(teamColor, ChessPiece.PieceType.KING));
    }

    /**
     * Checks if any of a given team's pieces is capable of moving without causing check
     *
     * @param teamColor which team to check for safe moves
     * @return True if the specified team is in checkmate
     */
    private boolean hasSafeMoves(TeamColor teamColor) {
        for (var position : board) {
            ChessPiece piece = board.getPiece(position);
            // Check every one of our pieces
            if (piece.getTeamColor() == teamColor) {
                // Try every move to see if there is any spot not in check
                for (var move : piece.pieceMoves(board, position)) {
                    ChessBoard newBoard = board.cloneWithMove(move);
                    if (!isInCheck(newBoard, teamColor, newBoard.findFirstPositionOf(teamColor, ChessPiece.PieceType.KING))) {
                        // Found a safe move!
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && !hasSafeMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && !hasSafeMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", board=" + board +
                '}';
    }
}
