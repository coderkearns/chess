package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    public ChessPosition delta(int rowDelta, int colDelta) {
        return new ChessPosition(row + rowDelta, col + colDelta);
    }

    /**
     * Converts a chess coordinate (such as a1 or h8) to a ChessPosition object.
     *
     * @return the ChessPosition represented by those coords
     */
    public static ChessPosition fromCoordinates(String coords) throws IndexOutOfBoundsException {
        String columnChar = coords.substring(0, 1);
        String rowChar = coords.substring(1, 2);

        int column = "abcdefgh".indexOf(columnChar) + 1;
        if (column == 0) {
            throw new IndexOutOfBoundsException();
        }
        int row = Integer.parseInt(rowChar);
        if (row < 1 || row > 8) {
            throw new IndexOutOfBoundsException();
        }

        return new ChessPosition(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
