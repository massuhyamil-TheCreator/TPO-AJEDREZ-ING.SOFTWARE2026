package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KnightTest {

    @Test
    void knightMovesInLShape() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(4, 4);
        Knight knight = new Knight(PieceColor.WHITE);
        board.placePiece(from, knight);

        List<Position> reachable = knight.getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(6, 5)));
        assertTrue(reachable.contains(new Position(2, 3)));
        assertEquals(8, reachable.size());
    }

    @Test
    void knightCanJumpOverPieces() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(0, 1);
        board.placePiece(from, new Knight(PieceColor.WHITE));
        board.placePiece(new Position(1, 0), new Pawn(PieceColor.WHITE));
        board.placePiece(new Position(1, 1), new Pawn(PieceColor.WHITE));
        board.placePiece(new Position(1, 2), new Pawn(PieceColor.WHITE));

        List<Position> reachable = board.getPieceAt(from).getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(2, 0)));
        assertTrue(reachable.contains(new Position(2, 2)));
    }
}
