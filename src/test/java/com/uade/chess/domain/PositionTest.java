package com.uade.chess.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositionTest {

    @Test
    void positionsWithSameCoordinatesAreEqual() {
        Position a = new Position(3, 4);
        Position b = new Position(3, 4);

        assertEquals(a, b);
    }

    @Test
    void isWithinReturnsTrueForValidCoordinates() {
        Position position = new Position(0, 0);

        assertTrue(position.isWithin(BoardDimensions.STANDARD));
    }

    @Test
    void isWithinReturnsFalseWhenOutOfBounds() {
        Position position = new Position(8, 0);

        assertFalse(position.isWithin(BoardDimensions.STANDARD));
    }
}
