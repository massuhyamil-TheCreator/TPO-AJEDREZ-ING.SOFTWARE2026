package com.uade.chess.gui;

import com.uade.chess.domain.Move;
import com.uade.chess.domain.PieceColor;
import com.uade.chess.domain.pieces.Piece;

/** Registro de presentación; no modifica ni extiende el historial del dominio. */
public record MoveEntry(PieceColor player, Piece piece, Move move,
                        Piece capturedPiece, boolean check) {
    public String notation() {
        return move.from() + (capturedPiece == null ? " → " : " × ")
                + move.to() + (check ? " +" : "");
    }
}
