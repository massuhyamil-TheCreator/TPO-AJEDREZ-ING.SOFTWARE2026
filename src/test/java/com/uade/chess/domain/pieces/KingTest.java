package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KingTest {

    @Test
    void kingMovesOneSquareInAnyDirection() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(4, 4);
        King king = new King(PieceColor.WHITE);
        board.placePiece(from, king);

        List<Position> reachable = king.getReachablePositions(from, board);

        assertEquals(8, reachable.size());
        assertTrue(reachable.contains(new Position(5, 5)));
        assertFalse(reachable.contains(new Position(6, 6))); // dos casillas, no válido para el rey
    }
}
