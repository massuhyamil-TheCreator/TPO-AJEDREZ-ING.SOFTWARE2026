package com.uade.chess.domain;

import com.uade.chess.domain.pieces.Piece;

/**
 * Resultado explícito de intentar un movimiento. A propósito NO se usan
 * excepciones para movimientos inválidos esperables (un movimiento ilegal
 * es parte normal del flujo de un juego de ajedrez, no un error excepcional).
 */
public final class MoveResult {
    private final boolean success;
    private final MoveFailureReason failureReason;
    private final Piece capturedPiece;
    private final boolean check;

    private MoveResult(boolean success, MoveFailureReason failureReason, Piece capturedPiece, boolean check) {
        this.success = success;
        this.failureReason = failureReason;
        this.capturedPiece = capturedPiece;
        this.check = check;
    }

    public static MoveResult ok(Piece capturedPiece, boolean check) {
        return new MoveResult(true, null, capturedPiece, check);
    }

    public static MoveResult fail(MoveFailureReason reason) {
        return new MoveResult(false, reason, null, false);
    }

    public boolean isSuccess() {
        return success;
    }

    public MoveFailureReason getFailureReason() {
        return failureReason;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isCheck() {
        return check;
    }
}
