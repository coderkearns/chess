package passoff.chess;

import chess.ChessPosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChessPositionTests extends EqualsTestingUtility<ChessPosition> {
    public ChessPositionTests() {
        super("ChessPosition", "positions");
    }

    @Override
    protected ChessPosition buildOriginal() {
        return new ChessPosition(3, 7);
    }

    @Override
    protected Collection<ChessPosition> buildAllDifferent() {
        return List.of(
                new ChessPosition(7, 3),
                new ChessPosition(6, 3),
                new ChessPosition(4, 3),
                new ChessPosition(3, 1),
                new ChessPosition(3, 2),
                new ChessPosition(3, 3)
        );
    }

    @Test
    @DisplayName("Make From Coordinates")
    public void testFromCoordinates() {
        for (var test : Map.of(
                "a2", new ChessPosition(2, 1),
                "h3", new ChessPosition(3, 8)
        ).entrySet()) {
            assertEquals(test.getValue(), ChessPosition.fromCoordinates(test.getKey()));
        }

        assertThrows(IndexOutOfBoundsException.class, () -> ChessPosition.fromCoordinates("i2"));
        assertThrows(IndexOutOfBoundsException.class, () -> ChessPosition.fromCoordinates("a9"));

    }
}
