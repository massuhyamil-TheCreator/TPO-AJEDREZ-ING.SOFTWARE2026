package com.uade.chess.domain.pieces;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PawnTest {

    @Test
    void whitePawnCanMoveTwoSquaresFromStartingRow() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(1, 4);
        Pawn pawn = new Pawn(PieceColor.WHITE);
        board.placePiece(from, pawn);

        List<Position> reachable = pawn.getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(2, 4)));
        assertTrue(reachable.contains(new Position(3, 4)));
    }

    @Test
    void pawnCannotMoveTwoSquaresAfterLeavingStartingRow() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(2, 4);
        Pawn pawn = new Pawn(PieceColor.WHITE);
        board.placePiece(from, pawn);

        List<Position> reachable = pawn.getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(3, 4)));
        assertFalse(reachable.contains(new Position(4, 4)));
    }

    @Test
    void pawnCannotMoveForwardIntoOccupiedSquare() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(1, 4);
        board.placePiece(from, new Pawn(PieceColor.WHITE));
        board.placePiece(new Position(2, 4), new Pawn(PieceColor.BLACK));

        List<Position> reachable = board.getPieceAt(from).getReachablePositions(from, board);

        assertFalse(reachable.contains(new Position(2, 4)));
    }

    @Test
    void pawnCapturesOnlyDiagonally() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(1, 4);
        board.placePiece(from, new Pawn(PieceColor.WHITE));
        board.placePiece(new Position(2, 5), new Pawn(PieceColor.BLACK));
        board.placePiece(new Position(2, 4), new Pawn(PieceColor.BLACK)); // bloquea el avance recto

        List<Position> reachable = board.getPieceAt(from).getReachablePositions(from, board);

        assertTrue(reachable.contains(new Position(2, 5)));
        assertFalse(reachable.contains(new Position(2, 4))); // no puede "capturar" derecho
    }

    @Test
    void pawnThreatensDiagonalsEvenWhenEmpty() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(4, 4);
        Pawn pawn = new Pawn(PieceColor.WHITE);
        board.placePiece(from, pawn);

        List<Position> threatened = pawn.getThreatenedPositions(from, board);

        assertTrue(threatened.contains(new Position(5, 3)));
        assertTrue(threatened.contains(new Position(5, 5)));
        assertFalse(threatened.contains(new Position(5, 4))); // no amenaza derecho, solo avanza
    }
}
