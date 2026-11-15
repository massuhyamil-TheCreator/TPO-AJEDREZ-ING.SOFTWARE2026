package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class QueenTest {

    @Test
    void queenCombinesRookAndBishopMovement() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(3, 3);
        Queen queen = new Queen(PieceColor.WHITE);
        board.placePiece(from, queen);

        List<Position> reachable = queen.getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(3, 7))); // horizontal, como torre
        assertTrue(reachable.contains(new Position(7, 3))); // vertical, como torre
        assertTrue(reachable.contains(new Position(6, 6))); // diagonal, como alfil
    }
}
