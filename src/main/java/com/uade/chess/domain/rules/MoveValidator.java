package com.uade.chess.domain.rules;

import com.uade.chess.domain.Board;
import com.uade.chess.domain.Move;
import com.uade.chess.domain.MoveFailureReason;
import com.uade.chess.domain.MoveResult;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.Position;
import com.uade.chess.domain.pieces.Piece;

/**
 * Única responsabilidad: dado un Move y un Board, decidir si es legal
 * y, si lo es, aplicarlo. Acá vive el flujo completo descripto en la
 * Fase 0: límites -> origen vacío -> turno -> fuego amigo -> movimiento
 * propio de la pieza (bloqueos incluidos) -> self-check -> aplicar.
 */
public final class MoveValidator {

    private final CheckDetector checkDetector;

    public MoveValidator(CheckDetector checkDetector) {
        this.checkDetector = checkDetector;
    }

    public MoveResult validate(Move move, Board board, PieceColor currentTurn) {
        Position from = move.from();
        Position to = move.to();

        if (!from.isWithin(board.getDimensions()) || !to.isWithin(board.getDimensions())) {
            return MoveResult.fail(MoveFailureReason.OUT_OF_BOUNDS);
        }

        Piece piece = board.getPieceAt(from);
        if (piece == null) {
            return MoveResult.fail(MoveFailureReason.EMPTY_ORIGIN);
        }

        if (piece.getColor() != currentTurn) {
            return MoveResult.fail(MoveFailureReason.NOT_YOUR_TURN);
        }

        if (board.isOccupiedByAlly(to, currentTurn)) {
            return MoveResult.fail(MoveFailureReason.FRIENDLY_FIRE);
        }

        if (!piece.getReachablePositions(from, board).contains(to)) {
            return MoveResult.fail(MoveFailureReason.INVALID_PIECE_MOVEMENT);
        }

        // Self-check: simular sobre un clon del tablero, nunca sobre el real.
        Board simulation = board.clone();
        simulation.movePiece(from, to);
        if (checkDetector.isInCheck(simulation, currentTurn)) {
            return MoveResult.fail(MoveFailureReason.SELF_CHECK);
        }

        Piece capturedPiece = board.getPieceAt(to);
        board.movePiece(from, to);

        boolean opponentInCheck = checkDetector.isInCheck(board, currentTurn.opposite());

        return MoveResult.ok(capturedPiece, opponentInCheck);
    }
}
