package com.uade.chess.domain;

import com.uade.chess.domain.pieces.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    @Test
    void newBoardHasNoPieces() {
        Board board = new Board(BoardDimensions.STANDARD);

        assertTrue(board.isEmpty(new Position(0, 0)));
    }

    @Test
    void placePieceMakesSquareOccupied() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 0), new Rook(PieceColor.WHITE));

        assertFalse(board.isEmpty(new Position(0, 0)));
        assertEquals(PieceColor.WHITE, board.getPieceAt(new Position(0, 0)).getColor());
    }

    @Test
    void movePieceClearsOriginAndOccupiesDestination() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position from = new Position(0, 0);
        Position to = new Position(3, 0);
        board.placePiece(from, new Rook(PieceColor.WHITE));

        board.movePiece(from, to);

        assertTrue(board.isEmpty(from));
        assertFalse(board.isEmpty(to));
    }

    @Test
    void cloneIsIndependentFromOriginal() {
        Board board = new Board(BoardDimensions.STANDARD);
        Position position = new Position(0, 0);
        board.placePiece(position, new Rook(PieceColor.WHITE));

        Board clone = board.clone();
        clone.removePiece(position);

        assertFalse(board.isEmpty(position));
        assertTrue(clone.isEmpty(position));
    }
}
