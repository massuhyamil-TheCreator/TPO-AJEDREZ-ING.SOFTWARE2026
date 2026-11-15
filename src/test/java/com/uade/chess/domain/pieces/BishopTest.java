package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BishopTest {

    @Test
    void bishopMovesOnlyDiagonally() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(3, 3);
        Bishop bishop = new Bishop(PieceColor.WHITE);
        board.placePiece(from, bishop);

        List<Position> reachable = bishop.getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(6, 6)));
        assertTrue(reachable.contains(new Position(0, 0)));
        assertFalse(reachable.contains(new Position(3, 6))); // horizontal, no válido para alfil
        assertFalse(reachable.contains(new Position(6, 3))); // vertical, no válido para alfil
    }
}
