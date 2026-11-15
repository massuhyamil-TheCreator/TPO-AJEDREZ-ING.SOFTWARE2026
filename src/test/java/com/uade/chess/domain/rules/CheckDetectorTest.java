package com.uade.chess.domain.rules;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.Bishop;
import com.uade.chess.domain.pieces.King;
import com.uade.chess.domain.pieces.Knight;
import com.uade.chess.domain.pieces.Pawn;
import com.uade.chess.domain.pieces.Queen;
import com.uade.chess.domain.pieces.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckDetectorTest {

    private final CheckDetector checkDetector = new CheckDetector();

    @Test
    void kingNotInCheckWhenNoThreats() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 4), new King(PieceColor.BLACK));

        assertFalse(checkDetector.isInCheck(board, PieceColor.WHITE));
    }

    @Test
    void kingInCheckFromRook() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 4), new King(PieceColor.BLACK));
        board.placePiece(new Position(5, 4), new Rook(PieceColor.BLACK));

        assertTrue(checkDetector.isInCheck(board, PieceColor.WHITE));
    }

    @Test
    void kingInCheckFromKnight() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 0), new King(PieceColor.BLACK));
        board.placePiece(new Position(2, 5), new Knight(PieceColor.BLACK));

        assertTrue(checkDetector.isInCheck(board, PieceColor.WHITE));
    }

    @Test
    void kingInCheckFromBishop() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 0), new King(PieceColor.BLACK));
        board.placePiece(new Position(3, 7), new Bishop(PieceColor.BLACK));

        assertTrue(checkDetector.isInCheck(board, PieceColor.WHITE));
    }

    @Test
    void kingInCheckFromQueen() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 0), new King(PieceColor.BLACK));
        board.placePiece(new Position(0, 0), new Queen(PieceColor.BLACK));

        assertTrue(checkDetector.isInCheck(board, PieceColor.WHITE));
    }

    @Test
    void kingInCheckFromPawnDiagonal() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(4, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 0), new King(PieceColor.BLACK));
        board.placePiece(new Position(5, 5), new Pawn(PieceColor.BLACK));

        assertTrue(checkDetector.isInCheck(board, PieceColor.WHITE));
    }

    @Test
    void blockingPieceStopsCheckFromSlidingPiece() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 4), new King(PieceColor.BLACK));
        board.placePiece(new Position(5, 4), new Rook(PieceColor.BLACK));
        board.placePiece(new Position(3, 4), new Pawn(PieceColor.WHITE)); // bloquea la línea de jaque

        assertFalse(checkDetector.isInCheck(board, PieceColor.WHITE));
    }
}
