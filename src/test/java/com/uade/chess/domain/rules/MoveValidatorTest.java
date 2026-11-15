package com.uade.chess.domain.rules;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.BoardDimensions;
import com.uade.chess.domain.Move;
import com.uade.chess.domain.MoveFailureReason;
import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.King;
import com.uade.chess.domain.pieces.Pawn;
import com.uade.chess.domain.pieces.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveValidatorTest {

    private final CheckDetector checkDetector = new CheckDetector();
    private final MoveValidator validator = new MoveValidator(checkDetector);

    @Test
    void validMoveSucceeds() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(1, 4), new Pawn(PieceColor.WHITE));
        placeBothKingsAwayFromDanger(board);

        MoveResult result = validator.validate(new Move(new Position(1, 4), new Position(3, 4)), board, PieceColor.WHITE);

        assertTrue(result.isSuccess());
    }

    @Test
    void movingFromEmptySquareFails() {
        Board board = new Board(BoardDimensions.STANDARD);
        placeBothKingsAwayFromDanger(board);

        MoveResult result = validator.validate(new Move(new Position(3, 3), new Position(4, 3)), board, PieceColor.WHITE);

        assertFalse(result.isSuccess());
        assertEquals(MoveFailureReason.EMPTY_ORIGIN, result.getFailureReason());
    }

    @Test
    void movingOpponentPieceFails() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(6, 4), new Pawn(PieceColor.BLACK));
        placeBothKingsAwayFromDanger(board);

        MoveResult result = validator.validate(new Move(new Position(6, 4), new Position(5, 4)), board, PieceColor.WHITE);

        assertFalse(result.isSuccess());
        assertEquals(MoveFailureReason.NOT_YOUR_TURN, result.getFailureReason());
    }

    @Test
    void capturingOwnPieceFails() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 0), new Rook(PieceColor.WHITE));
        board.placePiece(new Position(3, 0), new Rook(PieceColor.WHITE));
        placeBothKingsAwayFromDanger(board);

        MoveResult result = validator.validate(new Move(new Position(0, 0), new Position(3, 0)), board, PieceColor.WHITE);

        assertFalse(result.isSuccess());
        assertEquals(MoveFailureReason.FRIENDLY_FIRE, result.getFailureReason());
    }

    @Test
    void movementNotMatchingPieceFails() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 0), new Rook(PieceColor.WHITE));
        placeBothKingsAwayFromDanger(board);

        MoveResult result = validator.validate(new Move(new Position(0, 0), new Position(1, 1)), board, PieceColor.WHITE);

        assertFalse(result.isSuccess());
        assertEquals(MoveFailureReason.INVALID_PIECE_MOVEMENT, result.getFailureReason());
    }

    @Test
    void capturingSuccessfullyReturnsCapturedPiece() {
        Board board = new Board(BoardDimensions.STANDARD);
        board.placePiece(new Position(0, 0), new Rook(PieceColor.WHITE));
        board.placePiece(new Position(3, 0), new Rook(PieceColor.BLACK));
        placeBothKingsAwayFromDanger(board);

        MoveResult result = validator.validate(new Move(new Position(0, 0), new Position(3, 0)), board, PieceColor.WHITE);

        assertTrue(result.isSuccess());
        assertNotNull(result.getCapturedPiece());
        assertEquals(PieceColor.BLACK, result.getCapturedPiece().getColor());
    }

    private void placeBothKingsAwayFromDanger(Board board) {
        board.placePiece(new Position(0, 7), new King(PieceColor.WHITE));
        board.placePiece(new Position(7, 7), new King(PieceColor.BLACK));
    }
}
