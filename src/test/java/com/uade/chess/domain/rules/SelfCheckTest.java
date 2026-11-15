package com.uade.chess.domain.rules;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.Move;
import com.uade.chess.domain.MoveFailureReason;
import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.King;
import com.uade.chess.domain.pieces.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SelfCheckTest {

    private final CheckDetector checkDetector = new CheckDetector();
    private final MoveValidator validator = new MoveValidator(checkDetector);

    @Test
    void movingPinnedPieceOutOfTheLineIsRejected() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 4), new King(PieceColor.BLACK));
        board.placePiece(new Position(3, 4), new Rook(PieceColor.WHITE)); // torre propia "clavada"
        board.placePiece(new Position(6, 4), new Rook(PieceColor.BLACK)); // torre enemiga que clava

        // Mover la torre propia fuera de la columna 4 expondría al rey a la torre negra.
        MoveResult result = validator.validate(new Move(new Position(3, 4), new Position(3, 0)), board, PieceColor.WHITE);

        assertFalse(result.isSuccess());
        assertEquals(MoveFailureReason.SELF_CHECK, result.getFailureReason());
    }

    @Test
    void movingPinnedPieceAlongThePinLineIsAllowed() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 4), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 4), new King(PieceColor.BLACK));
        board.placePiece(new Position(3, 4), new Rook(PieceColor.WHITE));
        board.placePiece(new Position(6, 4), new Rook(PieceColor.BLACK));

        // Moverse dentro de la misma columna sigue bloqueando el jaque, así que es legal.
        MoveResult result = validator.validate(new Move(new Position(3, 4), new Position(4, 4)), board, PieceColor.WHITE);

        assertTrue(result.isSuccess());
    }
}
